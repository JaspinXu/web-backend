package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleQueryDTO extends PageQueryDTO {
    /**
     * 教师名称，模糊匹配
     */
    private String teacherName;
    /**
     * 课程名称，模糊匹配
     */
    private String courseName;
}
