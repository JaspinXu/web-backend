package redlib.backend.dto;

import lombok.Data;

@Data
public class LabDTO {
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
}
