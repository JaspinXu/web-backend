package redlib.backend.service.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import redlib.backend.dto.ScheduleDTO;
import redlib.backend.model.Schedule;
import redlib.backend.utils.FormatUtils;
import redlib.backend.vo.ScheduleVO;

import java.util.Map;

public class ScheduleUtils {
    /**
     * 规范并校验scheduleDTO
     *
     * @param scheduleDTO 对象
     */
    public static void validateSchedule(ScheduleDTO scheduleDTO) {
        FormatUtils.trimFieldToNull(scheduleDTO);
        Assert.notNull(scheduleDTO, "实验室输入数据不能为空");
        Assert.hasText(scheduleDTO.getLabName(), "实验室名称不能为空");
    }

    /**
     * 将实体对象转换为VO对象
     *
     * @param schedule 实体对象
     * @param nameMap 映射
     * @return VO对象
     */
    public static ScheduleVO convertToVO(Schedule schedule, Map<String, String> nameMap) {
        ScheduleVO scheduleVO = new ScheduleVO();
        BeanUtils.copyProperties(schedule, scheduleVO);

        scheduleVO.setCreatedByDesc(nameMap.get(schedule.getCreatedBy()));
        return scheduleVO;
    }
}
