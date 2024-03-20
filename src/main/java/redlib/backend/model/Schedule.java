package redlib.backend.model;

import java.util.Date;
import lombok.Data;

/**
 * 描述:schedule表的实体类
 * @version
 * @author:  19826
 * @创建时间: 2024-03-20
 */
@Data
public class Schedule {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 实验室名称
     */
    private String labName;

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
     * 学期名
     */
    private String semesterName;

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

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 删除标记
     */
    private Byte deleted;
}