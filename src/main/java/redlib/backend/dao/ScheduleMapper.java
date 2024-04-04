package redlib.backend.dao;

import org.apache.ibatis.annotations.Param;
import redlib.backend.dto.query.CheckQueryDTO;
import redlib.backend.dto.query.ScheduleQueryDTO;
import redlib.backend.model.Schedule;

import java.util.List;

public interface ScheduleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Schedule record);

    int insertSelective(Schedule record);

    Schedule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Schedule record);

    int updateByPrimaryKey(Schedule record);


    /**
     * 根据查询条件获取命中个数
     *
     * @param queryDTO 查询条件
     * @return 命中数量
     */
    Integer count(@Param("queryDTO") ScheduleQueryDTO queryDTO);

    /**
     * 根据查询条件获取实验安排列表
     *
     * @param queryDTO 查询条件
     * @param offset   开始位置
     * @param limit    记录数量
     * @return 实验安排列表
     */
    List<Schedule> list(
            @Param("queryDTO") ScheduleQueryDTO queryDTO,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );

    /**
     * 根据代码列表批量删除实验安排
     *
     * @param codeList id列表
     */
    void deleteByCodes(@Param("codeList") List<Integer> codeList);

    /**
     * 根据名称查询实验安排列表
     *
     * @param teacherName 教师名称，模糊匹配
     * @param courseName  课程名称，模糊匹配
     * @return 实验安排列表
     */
    List<Schedule> listByName(
            @Param("teacherName") String teacherName,
            @Param("courseName") String courseName);

    /**
     * 根据查询条件获取有交集实验安排列表
     *
     * @param queryDTO 查询条件
     * @param offset   开始位置
     * @param limit    记录数量
     * @return 实验安排列表
     */
    List<Schedule> listOccupy(
            @Param("queryDTO") CheckQueryDTO queryDTO,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );

    /**
     * 根据查询条件获取命中个数
     *
     * @param queryDTO 查询条件
     * @return 命中数量
     */
    Integer countOccupy(@Param("queryDTO") CheckQueryDTO queryDTO);
}