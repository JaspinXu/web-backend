package redlib.backend.service.utils;

import org.springframework.beans.BeanUtils;
import redlib.backend.dto.ScheduleDTO;
import redlib.backend.dto.query.CheckQueryDTO;

public class CheckUtils {
    public static ScheduleDTO checkQueryDTOToScheduleDTO(CheckQueryDTO checkQueryDTO) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(checkQueryDTO, scheduleDTO);
        scheduleDTO.setLabName(null);
        scheduleDTO.setSemesterName(null);
        return scheduleDTO;
    }
}
