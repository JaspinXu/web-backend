package redlib.backend.model;

import java.util.Date;
import lombok.Data;

/**
 * 描述:lab表的实体类
 * @version
 * @author:  19826
 * @创建时间: 2024-03-12
 */
@Data
public class Lab {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String labCode;

    /**
     * 
     */
    private String labName;

    /**
     * 
     */
    private Integer studentNum;

    /**
     * 
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
}