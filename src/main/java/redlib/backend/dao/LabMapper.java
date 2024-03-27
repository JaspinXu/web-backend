package redlib.backend.dao;

import org.apache.ibatis.annotations.Param;
import redlib.backend.dto.query.DepartmentQueryDTO;
import redlib.backend.dto.query.LabQueryDTO;
import redlib.backend.model.Department;
import redlib.backend.model.Lab;

import java.util.List;

public interface LabMapper {
    int deleteByPrimaryKey(Integer id);

    /**
     * 新增记录
     *
     * @param record
     * @return
     */
    int insert(Lab record);

    int insertSelective(Lab record);

    Lab selectByPrimaryKey(Integer id);

    Lab selectByOccupy(Byte occupy);

    int updateByPrimaryKeySelective(Lab record);

    /**
     * 根据主键更新记录
     *
     * @param record
     * @return
     */
    int updateByPrimaryKey(Lab record);

    /**
     * 根据实验室编码获取实验室信息详情
     *
     * @param labCode 实验室编码
     * @return 实验室信息详情
     */
    Lab getByCode(
            @Param("labCode") String labCode);

    /**
     * 根据查询条件获取命中个数
     *
     * @param queryDTO 查询条件
     * @return 命中数量
     */
    Integer count(LabQueryDTO queryDTO);

    /**
     * 根据获取当前表格实验室总数目
     *
     * @return 总数量
     */
    Integer countSum();


    /**
     * 根据查询条件获取实验室列表
     *
     * @param queryDTO 查询条件
     * @param offset   开始位置
     * @param limit    记录数量
     * @return 实验室列表
     */
    List<Lab> list(
            @Param("queryDTO") LabQueryDTO queryDTO,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit
    );

    /**
     * 获取所有实验室列表
     *
     * @return 实验室列表
     */
    List<Lab> listAll();

    /**
     * 根据代码列表批量删除实验室
     *
     * @param codeList id列表
     */
    void deleteByCodes(@Param("codeList") List<Integer> codeList);

    /**
     * 根据实验室代码列表获取实验室信息列表
     *
     * @param codeList   实验室代码列表
     * @return 实验室列表
     */
    List<Lab> listByCodes(
            @Param("codeList") List<String> codeList);

    /**
     * 根据实验室名称查询实验室列表
     *
     * @param labName 实验室名称，模糊匹配
     * @return 实验室列表
     */
    List<Lab> listByName(
            @Param("labName") String labName);

    /**
     * 根据实验室名称列表查询实验室信息列表
     *
     * @param nameList 实验室名称列表
     * @return 实验室列表
     */
    List<Lab> listByNames(
            @Param("labName") List<String> nameList);
}