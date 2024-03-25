package redlib.backend.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import redlib.backend.annotation.BackendModule;
import redlib.backend.dao.AdminMapper;
import redlib.backend.dao.AdminPrivMapper;
import redlib.backend.dto.AdminDTO;
import redlib.backend.dto.AdminModDTO;
import redlib.backend.dto.query.KeywordQueryDTO;
import redlib.backend.model.Admin;
import redlib.backend.model.AdminPriv;
import redlib.backend.model.Page;
import redlib.backend.model.Token;
import redlib.backend.service.AdminService;
import redlib.backend.service.utils.AdminUtils;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.ThreadContextHolder;
import redlib.backend.vo.AdminVO;
import redlib.backend.vo.ModuleVO;
import redlib.backend.vo.PrivilegeVO;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminPrivMapper adminPrivMapper;

    @Override                   //这个方法的主要作用是获取系统中所有模块的信息，包括模块名和模块的权限列表
    public List<ModuleVO> listModules() {
        List<ModuleVO> moduleVOList = new ArrayList<>();
        Map<String, Object> serviceBeansMap = applicationContext.getBeansWithAnnotation(BackendModule.class);
        for (Object bean : serviceBeansMap.values()) {
            Class<?> cls = bean.getClass();
            BackendModule moduleAnnotation = AnnotationUtils.findAnnotation(cls, BackendModule.class);
            if (moduleAnnotation == null) {
                continue;
            }

            String className = AdminUtils.getModuleName(cls);
            ModuleVO moduleVO = new ModuleVO();
            moduleVO.setId(className);
            moduleVO.setPrivilegeList(Arrays.stream(moduleAnnotation.value()).map(item -> {
                PrivilegeVO privilegeVO = new PrivilegeVO();
                String[] pairs = item.split(":");
                Assert.isTrue(pairs.length == 2, cls.getName() + "privileges error");
                privilegeVO.setId(pairs[0]);
                privilegeVO.setDescription(pairs[1]);
                return privilegeVO;
            }).collect(Collectors.toList()));

            moduleVOList.add(moduleVO);
        }

        return moduleVOList;
    }

    @Override                   //这个方法的主要作用是根据一组管理员ID，快速获取对应的管理员名称
    public Map<Integer, String> getNameMap(Set<Integer> adminIds) {
        Map<Integer, String> adminMap;
        if (!CollectionUtils.isEmpty(adminIds)) {
            List<Admin> admins = adminMapper.listByIds(new ArrayList<>(adminIds));
            adminMap = admins.stream().collect(Collectors.toMap(Admin::getId, Admin::getName));
        } else {
            adminMap = new HashMap<>();
        }

        return adminMap;
    }


    @Override                      //这个方法的主要作用是根据查询条件获取管理员的分页列表
    public Page<AdminVO> list(KeywordQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new KeywordQueryDTO();
        } else {
            FormatUtils.trimFieldToNull(queryDTO);
        }

        queryDTO.setKeyword(FormatUtils.makeFuzzySearchTerm(queryDTO.getKeyword()));
        queryDTO.setOrderBy(FormatUtils.formatOrderBy(queryDTO.getOrderBy()));

        Integer total = adminMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), total);
        if (pageUtils.isDataEmpty()) {
            return pageUtils.getNullPage();
        }

        List<Admin> list = adminMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());

        Set<Integer> adminIds = list.stream().map(Admin::getUpdatedBy).collect(Collectors.toSet());
        adminIds.addAll(list.stream().map(Admin::getCreatedBy).collect(Collectors.toSet()));
        Map<Integer, String> nameMap = getNameMap(adminIds);

        List<AdminVO> voList = list.stream()
                .map(item -> AdminUtils.convertToBriefVO(item, nameMap))
                .collect(Collectors.toList());
        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    @Override                     //这个方法的主要作用是根据管理员ID获取管理员的详细信息，包括管理员的基本信息和权限列表
    public AdminDTO getDetail(Integer id) {
        Assert.notNull(id, "id不能为空");
        Admin admin = adminMapper.selectByPrimaryKey(id);
        Assert.notNull(admin, "未找到管理员: " + id);
        List<AdminPriv> privList = adminPrivMapper.list(id);

        AdminDTO adminDTO = new AdminDTO();
        BeanUtils.copyProperties(admin, adminDTO);
        adminDTO.setPassword(null);
        Map<String, List<String>> modMap = new HashMap<>();
        privList.forEach(item -> {
            List<String> list = modMap.computeIfAbsent(item.getModId(), k -> new ArrayList<>());
            list.add(item.getPriv());
        });

        List<AdminModDTO> modDTOList = new ArrayList<>();
        modMap.entrySet().forEach(entry -> {
            AdminModDTO modDTO = new AdminModDTO();
            modDTO.setId(entry.getKey());
            modDTO.setPrivList(entry.getValue());
            modDTOList.add(modDTO);
        });

        adminDTO.setModList(modDTOList);
        return adminDTO;
    }

    @Override
    @Transactional                  //这个方法的主要作用是更新管理员的信息，包括管理员的基本信息和权限
    public Integer update(AdminDTO adminDTO) {
        AdminUtils.validate(adminDTO);
        Assert.notNull(adminDTO.getId(), "管理员id不能为空");
        Admin admin = adminMapper.selectByPrimaryKey(adminDTO.getId());
        Assert.notNull(admin, "未找到管理员，id=" + adminDTO.getId());

        Token token = ThreadContextHolder.getToken();
        if ("root".equalsIgnoreCase(adminDTO.getUserCode())) {
            Assert.isTrue("root".equalsIgnoreCase(token.getUserCode()), "只有root用户可以修改root信息");
        }

        BeanUtils.copyProperties(adminDTO, admin);
        admin.setUpdatedBy(token.getUserId());
        adminMapper.updateByPrimaryKey(admin);
        List<Integer> ids = new ArrayList<>();
        ids.add(admin.getId());
        adminPrivMapper.deleteByAdminIds(ids);
        updateOtherInfo(adminDTO);
        return admin.getId();
    }

    @Override
    @Transactional                     //这个方法的主要作用是添加新的管理员，包括管理员的基本信息和权限。
    public Integer add(AdminDTO adminDTO) {
        AdminUtils.validate(adminDTO);
        Assert.hasText(adminDTO.getPassword(), "密码不能为空");
        Admin admin = adminMapper.getByUserCode(adminDTO.getUserCode());
        Assert.isNull(admin, "该用户ID已经存在，请另外指定一个");
        admin = new Admin();
        BeanUtils.copyProperties(adminDTO, admin);
        Token token = ThreadContextHolder.getToken();
        admin.setUpdatedBy(token.getUserId());
        admin.setCreatedBy(token.getUserId());
        adminMapper.insert(admin);
        adminDTO.setId((admin.getId()));
        updateOtherInfo(adminDTO);
        return admin.getId();
    }

    @Override                          //这个方法的主要作用是删除一组管理员，包括管理员的基本信息和权限
    public Integer delete(List<Integer> ids) {
        Assert.notEmpty(ids, "删除列表不能为空");
        int size = adminMapper.delete(ids);
        adminPrivMapper.deleteByAdminIds(ids);
        return size;
    }

    @Override                         //这个方法的主要作用是更新管理员的密码
    public void updatePassword(String oldPassword, String password) {
        Token token = ThreadContextHolder.getToken();
        Admin admin = adminMapper.login(token.getUserCode(), FormatUtils.password(oldPassword));
        Assert.notNull(admin, "旧密码错误");
        admin.setPassword(FormatUtils.password(password));
        adminMapper.updateByPrimaryKey(admin);
    }

    private void updateOtherInfo(AdminDTO adminDTO) {         //这个方法的主要作用是更新管理员的模块和权限信息
        if ("root".equalsIgnoreCase(adminDTO.getUserCode())) {
            return;
        }

        if (CollectionUtils.isEmpty(adminDTO.getModList())) {
            return;
        }

        for (AdminModDTO modDTO : adminDTO.getModList()) {
            for (String priv : modDTO.getPrivList()) {
                AdminPriv adminPriv = new AdminPriv();
                adminPriv.setAdminId(adminDTO.getId());
                adminPriv.setModId(modDTO.getId());
                adminPriv.setPriv(priv);
                adminPrivMapper.insert(adminPriv);
            }
        }
    }
}
