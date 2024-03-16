package redlib.backend.model;

import java.util.Date;
import lombok.Data;

/**
 * ����:lab���ʵ����
 * @version
 * @author:  19826
 * @����ʱ��: 2024-03-16
 */
@Data
public class Lab {
    /**
     * 
     */
    private Integer id;

    /**
     * ʵ���ұ���
     */
    private String labCode;

    /**
     * ʵ��������
     */
    private String labName;

    /**
     * ʵ�������������
     */
    private Integer studentMax;

    /**
     * ��ע
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

    /**
     * ɾ�����
     */
    private Byte deleted;
}