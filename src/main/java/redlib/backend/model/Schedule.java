package redlib.backend.model;

import java.util.Date;
import lombok.Data;

/**
 * 描述:schedule表的实体类
 * @version
 * @author:  19826
 * @创建时间: 2024-03-12
 */
@Data
public class Schedule {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 预约单号
     */
    private String reservationCode;

    /**
     * 实验室编码
     */
    private String labCode;

    /**
     * 实验室名称
     */
    private String labName;

    /**
     * 实验教师编码
     */
    private String teacherCode;

    /**
     * 教师名称
     */
    private String teacherName;

    /**
     * 课程编码
     */
    private String courseCode;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 学生人数
     */
    private Integer studentNum;

    /**
     * 周次
     */
    private String courseWeek;

    /**
     * 星期
     */
    private String courseDay;

    /**
     * 节次
     */
    private String courseTime;

    /**
     * 实验课总次数
     */
    private Integer totalCount;

    /**
     * 联系电话
     */
    private String contactPhone;

    /**
     * 备注
     */
    private String description;

    /**
     * 预约单状态
     */
    private Byte status;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 删除标记
     */
    private Byte deleted;
}