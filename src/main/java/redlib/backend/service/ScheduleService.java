package redlib.backend.service;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Workbook;
import redlib.backend.dto.ScheduleDTO;
import redlib.backend.dto.SelectedKeysDTO;
import redlib.backend.dto.query.CheckQueryDTO;
import redlib.backend.dto.query.LabQueryDTO;
import redlib.backend.dto.query.ScheduleQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.vo.LabVO;
import redlib.backend.vo.ScheduleVO;

import java.io.InputStream;
import java.util.List;

public interface ScheduleService {

    /**
     * 分页获取安排信息
     *
     * @param queryDTO 查询条件和分页信息
     * @return 带分页信息的实验安排列表
     */
    Page<ScheduleVO> listByPage(ScheduleQueryDTO queryDTO);

    /**
     * 分页获取可用实验室信息
     *
     * @param queryDTO 查询条件和分页信息
     * @return 带分页信息的可用实验室列表
     */
    Page<LabVO> listFreeLab(CheckQueryDTO queryDTO);



    /**
     * 新建实验安排表
     *
     * @param scheduleDTO 实验安排表输入对象
     * @return 实验安排编码
     */
    Integer addSchedule(ScheduleDTO scheduleDTO);

    /**
     * 根据提供的字段信息和可用实验室以及最新学期名创建实验安排表
     * 即选择可用实验室保存后自动创建的实验安排
     * @param queryDTO 预置安排or查询请求输入对象
     * @param keysDTO  选择的可用实验室id列表
     * //@return 成功与否
     */
    Integer addScheduleAuto(
            CheckQueryDTO queryDTO,
            SelectedKeysDTO keysDTO
    );

    ScheduleDTO getById(Integer id);

    /**
     * 更新实验表数据
     *
     * @param scheduleDTO 实验表输入对象
     * @return 实验室编码
     */
    Integer updateSchedule(ScheduleDTO scheduleDTO);

    /**
     * 根据编码列表，批量删除实验表
     *
     * @param ids 编码列表
     */
    void deleteByCodes(List<Integer> ids);

    Workbook export(ScheduleQueryDTO queryDTO);

    int importSchedule(
            InputStream inputStream,
            String fileName) throws Exception;
}
