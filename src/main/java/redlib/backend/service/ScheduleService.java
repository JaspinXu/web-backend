package redlib.backend.service;

import org.apache.poi.ss.usermodel.Workbook;
import redlib.backend.dto.ScheduleDTO;
import redlib.backend.dto.query.ScheduleQueryDTO;
import redlib.backend.model.Page;
import redlib.backend.vo.ScheduleVO;

import java.io.InputStream;
import java.util.List;

public interface ScheduleService {
    Page<ScheduleVO> listByPage(ScheduleQueryDTO queryDTO);

    /**
     * 新建实验安排表
     *
     * @param scheduleDTO 实验安排表输入对象
     * @return 实验室编码
     */
    Integer addSchedule(ScheduleDTO scheduleDTO);

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
