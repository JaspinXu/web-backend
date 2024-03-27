package redlib.backend.service.impl;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import redlib.backend.dao.LabMapper;
import redlib.backend.dto.LabDTO;
import redlib.backend.dto.query.LabQueryDTO;
import redlib.backend.model.Lab;
import redlib.backend.model.Page;
import redlib.backend.model.Token;
import redlib.backend.service.AdminService;
import redlib.backend.service.LabService;
import redlib.backend.service.utils.LabUtils;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.utils.ThreadContextHolder;
import redlib.backend.utils.XlsUtils;
import redlib.backend.vo.LabVO;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class LabServiceImpl implements LabService {
    @Autowired
    private LabMapper labMapper;

    @Autowired
    private AdminService adminService;

    /**
     * 分页获取实验室信息
     *
     * @param queryDTO 查询条件和分页信息
     * @return 带分页信息的实验室数据列表
     */
    @Override
    public Page<LabVO> listByPage(LabQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new LabQueryDTO();
        }

        queryDTO.setLabName(FormatUtils.makeFuzzySearchTerm(queryDTO.getLabName()));
        Integer size = labMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }

        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Lab> list = labMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());

        // 提取list列表中的创建人字段，到一个Set集合中去
        Set<Integer> adminIds = list.stream().map(Lab::getCreatedBy).collect(Collectors.toSet());

        // 提取list列表中的更新人字段，追加到集合中去
        adminIds.addAll(list.stream().map(Lab::getCreatedBy).collect(Collectors.toSet()));

        // 获取id到人名的映射
        Map<Integer, String> nameMap = adminService.getNameMap(adminIds);

        List<LabVO> voList = new ArrayList<>();
        for (Lab lab : list) {
            // Lab对象转VO对象
            LabVO vo = LabUtils.convertToVO(lab, nameMap);
            voList.add(vo);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }

    @Override                   //这个方法的主要作用是根据一组实验室名称，快速获取对应的实验室编码
    public Map<String, String> getCodeMap(Set<String> labsFree) {
        Map<String, String> labMap;
        if (!CollectionUtils.isEmpty(labsFree)) {
            List<Lab> labs = labMapper.listByNames(new ArrayList<>(labsFree));
            labMap = labs.stream().collect(Collectors.toMap(Lab::getLabName, Lab::getLabCode));
        } else {
            labMap = new HashMap<>();
        }

        return labMap;
    }

    /**
     * 新建实验室
     *
     * @param labDTO 实验室输入对象
     * @return 实验室编码
     */
    @Override
    public Integer addLab(LabDTO labDTO) {
        Token token = ThreadContextHolder.getToken();
        // 校验输入数据正确性
        LabUtils.validateLab(labDTO);
        // 创建实体对象，用以保存到数据库
        Lab lab = new Lab();
        // 将输入的字段全部复制到实体对象中
        BeanUtils.copyProperties(labDTO, lab);
        lab.setCreatedAt(new Date());
        lab.setUpdatedAt(new Date());
        lab.setCreatedBy(token.getUserId());
        lab.setUpdatedBy(token.getUserId());
        // 调用DAO方法保存到数据库表
        labMapper.insert(lab);
        return lab.getId();
    }

    @Override
    public LabDTO getById(Integer id) {
        Assert.notNull(id, "请提供id");
        Assert.notNull(id, "实验室id不能为空");
        Lab lab = labMapper.selectByPrimaryKey(id);
        Assert.notNull(lab, "id不存在");
        LabDTO dto = new LabDTO();
        BeanUtils.copyProperties(lab, dto);
        return dto;
    }

    /**
     * 更新实验室数据
     *
     * @param labDTO 实验室输入对象
     * @return 实验室编码
     */
    @Override
    public Integer updateLab(LabDTO labDTO) {
        // 校验输入数据正确性
        Token token = ThreadContextHolder.getToken();
        LabUtils.validateLab(labDTO);
        Assert.notNull(labDTO.getId(), "实验室id不能为空");
        Lab lab = labMapper.selectByPrimaryKey(labDTO.getId());
        Assert.notNull(lab, "没有找到实验室，Id为：" + labDTO.getId());

        BeanUtils.copyProperties(labDTO, lab);
        lab.setUpdatedBy(token.getUserId());
        lab.setUpdatedAt(new Date());
        labMapper.updateByPrimaryKey(lab);
        return lab.getId();
    }

    /**
     * 根据编码列表，批量删除实验室
     *
     * @param ids 编码列表
     */
    @Override
    public void deleteByCodes(List<Integer> ids) {
        Assert.notEmpty(ids, "实验室id列表不能为空");
        labMapper.deleteByCodes(ids);
    }

    @Override
    public Workbook export(LabQueryDTO queryDTO) {
        queryDTO.setPageSize(100);
        Map<String, String> map = new LinkedHashMap<>();
        map.put("id", "实验室ID");
        map.put("labName", "实验室名称");
        map.put("studentMax", "实验人数最大容量");
        map.put("occupy", "占据状态");
        map.put("description", "备注");
        map.put("updatedAt", "更新时间");
        map.put("createdByDesc", "创建人");

        final AtomicBoolean finalPage = new AtomicBoolean(false);
        Workbook workbook = XlsUtils.exportToExcel(page -> {
            if (finalPage.get()) {
                return null;
            }
            queryDTO.setCurrent(page);
            List<LabVO> list = listByPage(queryDTO).getList();
            if (list.size() != 100) {
                finalPage.set(true);
            }
            return list;
        }, map);

        return workbook;
    }

    @Override
    public int importLab(InputStream inputStream, String fileName) throws Exception {
        Assert.hasText(fileName, "文件名不能为空");

        Map<String, String> map = new LinkedHashMap<>();
        map.put("实验室名称", "labName");
        map.put("实验人数最大容量", "studentMax");
        map.put("占据状态", "occupy");
        map.put("描述", "description");
        AtomicInteger row = new AtomicInteger(0);
        XlsUtils.importFromExcel(inputStream, fileName, (labDTO) -> {
            addLab(labDTO);
            row.incrementAndGet();
        }, map, LabDTO.class);

        return row.get();
    }
}
