package redlib.backend.service;

import redlib.backend.dto.SemesterDTO;
import redlib.backend.dto.query.SemesterQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.vo.SemesterVO;

import java.util.List;

public interface SemesterService {
    Page<SemesterVO> listByPage(SemesterQueryDTO queryDTO);

    /**
     * 新建学期表
     *
     * @param semesterDTO 学期表输入对象
     * @return 编码
     */
    Integer addSemester(SemesterDTO semesterDTO);

    SemesterDTO getById(Integer id);

    /**
     * 更新学期表数据
     *
     * @param semesterDTO 学期表输入对象
     * @return 编码
     */
    Integer updateSemester(SemesterDTO semesterDTO);

    /**
     * 根据编码列表，批量删除实验表
     *
     * @param ids 编码列表
     */
    void deleteByCodes(List<Integer> ids);
}
