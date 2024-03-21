package redlib.backend.dao;

import org.apache.ibatis.annotations.Param;
import redlib.backend.dto.query.SemesterQueryDTO;
import redlib.backend.model.Semester;

import java.util.List;

public interface SemesterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Semester record);

    int insertSelective(Semester record);

    Semester selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Semester record);

    int updateByPrimaryKey(Semester record);

    /**
     * 根据查询条件获取命中个数
     *
     * @param queryDTO 查询条件
     * @return 命中数量
     */
    Integer count(SemesterQueryDTO queryDTO);

    /**
     * 根据查询条件获取学期列表
     *
     * @param queryDTO 查询条件
     * @param offset   开始位置
     * @param limit    记录数量
     * @return 学期列表
     */
    List<Semester> list(
            @Param("queryDTO") SemesterQueryDTO queryDTO,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );

    /**
     * 根据代码列表批量删除学期
     *
     * @param codeList id列表
     */
    void deleteByCodes(@Param("codeList") List<Integer> codeList);

    /**
     * 根据名称查询学期列表
     *
     * @param semesterName 学期名，模糊匹配
     * @return 学期列表
     */
    List<Semester> listByName(
            @Param("semesterName") String semesterName);
}