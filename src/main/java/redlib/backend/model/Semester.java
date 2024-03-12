package redlib.backend.model;

import lombok.Data;

/**
 * 描述:semester表的实体类
 * @version
 * @author:  19826
 * @创建时间: 2024-03-12
 */
@Data
public class Semester {
    /**
     * 
     */
    private Integer id;

    /**
     * 学期编码
     */
    private String semesterCode;

    /**
     * 学期号
     */
    private String semesterNumber;

    /**
     * 开课周
     */
    private String semesterWeek;
}