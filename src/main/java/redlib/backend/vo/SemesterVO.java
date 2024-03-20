package redlib.backend.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SemesterVO {
    /**
     *
     */
    private Integer id;

    /**
     * 学期名
     */
    private String semesterName;

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

    private String createdByDesc;

    private String updatedByDesc;
}
