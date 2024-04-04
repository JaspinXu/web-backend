package redlib.backend.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import redlib.backend.dao.SemesterMapper;
import redlib.backend.dto.SemesterDTO;
import redlib.backend.dto.query.SemesterQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.model.Semester;
import redlib.backend.service.SemesterService;
import redlib.backend.service.utils.SemesterUtils;
import redlib.backend.utils.FormatUtils;
import redlib.backend.utils.PageUtils;
import redlib.backend.vo.SemesterVO;

import java.util.*;

@Service
public class SemesterServiceImpl implements SemesterService {
    @Autowired
    private SemesterMapper semesterMapper;


    /**
     * 分页获取学期表信息
     *
     * @param queryDTO 查询条件和分页信息
     * @return 带分页信息的列表
     */
    @Override
    public Page<SemesterVO> listByPage(SemesterQueryDTO queryDTO) {
        if (queryDTO == null) {
            queryDTO = new SemesterQueryDTO();
        }

        queryDTO.setSemesterName(FormatUtils.makeFuzzySearchTerm(queryDTO.getSemesterName()));
        Integer size = semesterMapper.count(queryDTO);
        PageUtils pageUtils = new PageUtils(queryDTO.getCurrent(), queryDTO.getPageSize(), size);

        if (size == 0) {
            // 没有命中，则返回空数据。
            return pageUtils.getNullPage();
        }

        // 利用myBatis到数据库中查询数据，以分页的方式
        List<Semester> list = semesterMapper.list(queryDTO, pageUtils.getOffset(), pageUtils.getLimit());

        List<SemesterVO> voList = new ArrayList<>();
        for (Semester semester : list) {
            // Semester对象转VO对象
            SemesterVO vo = SemesterUtils.convertToVO(semester);
            voList.add(vo);
        }

        return new Page<>(pageUtils.getCurrent(), pageUtils.getPageSize(), pageUtils.getTotal(), voList);
    }


    /**
     * 新建学期表
     *
     * @param semesterDTO 学期表输入对象
     * @return 编码
     */
    @Override
    public Integer addSemester(SemesterDTO semesterDTO) {
        // 校验输入数据正确性
        SemesterUtils.validateSemester(semesterDTO);
        // 创建实体对象，用以保存到数据库
        Semester semester = new Semester();
        // 将输入的字段全部复制到实体对象中
        BeanUtils.copyProperties(semesterDTO, semester);
        semester.setCreatedAt(new Date());
        semester.setUpdatedAt(new Date());
        // 调用DAO方法保存到数据库表
        semesterMapper.insert(semester);
        return semester.getId();
    }

    @Override
    public SemesterDTO getById(Integer id) {
        Assert.notNull(id, "请提供id");
        Assert.notNull(id, "学期id不能为空");
        Semester semester = semesterMapper.selectByPrimaryKey(id);
        Assert.notNull(semester, "id不存在");
        SemesterDTO dto = new SemesterDTO();
        BeanUtils.copyProperties(semester, dto);
        return dto;
    }

    /**
     * 更新学期表数据
     *
     * @param semesterDTO 学期输入对象
     * @return 编码
     */
    @Override
    public Integer updateSemester(SemesterDTO semesterDTO) {
        // 校验输入数据正确性
        SemesterUtils.validateSemester(semesterDTO);
        Assert.notNull(semesterDTO.getId(), "学期id不能为空");
        Semester semester = semesterMapper.selectByPrimaryKey(semesterDTO.getId());
        Assert.notNull(semester, "没有找到学期，Id为：" + semesterDTO.getId());

        BeanUtils.copyProperties(semesterDTO, semester);
        semester.setUpdatedAt(new Date());
        semesterMapper.updateByPrimaryKey(semester);
        return semester.getId();
    }

    /**
     * 根据编码列表，批量删除学期
     *
     * @param ids 编码列表
     */
    @Override
    public void deleteByCodes(List<Integer> ids) {
        Assert.notEmpty(ids, "学期id列表不能为空");
        semesterMapper.deleteByCodes(ids);
    }

}  
