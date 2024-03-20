package redlib.backend.vo;

import lombok.Data;

import java.util.Date;

@Data
public class LabVO {
    /**
     *
     */
    private Integer id;

    /**
     * 实验室编码
     */
    private String labCode;

    /**
     * 实验室名称
     */
    private String labName;

    /**
     * 实验人数最大容量
     */
    private Integer studentMax;

    /**
     * 占据状态
     */
    private Byte occupy;

    /**
     * 备注
     */
    private String description;

    /**
     * 创建人
     */
    private Integer createdBy;

    /**
     * 更新人
     */
    private Integer updatedBy;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    private String createdByDesc;

    private String updatedByDesc;
}
