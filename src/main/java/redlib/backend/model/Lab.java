package redlib.backend.model;

import java.util.Date;
import lombok.Data;

/**
 * ����:lab���ʵ����
 * @version
 * @author:  19826
 * @����ʱ��: 2024-03-12
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
     * ������
     */
    private Integer createdBy;

    /**
     * ������
     */
    private Integer updatedBy;

    /**
     * ����ʱ��
     */
    private Date createdAt;

    /**
     * ����ʱ��
     */
    private Date updatedAt;
}