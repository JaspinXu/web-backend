package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CheckQueryDTO extends PageQueryDTO {

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 教师名称
     */
    private String teacherName;

    /**
     * 节次
     */
    private String courseTime;

    /**
     * 周次
     */
    private String courseWeek;

    /**
     * 星期
     */
    private String courseDay;

    /**
     * 学生人数
     */
    private Integer studentNum;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 备注
     */
    private String description;
}
