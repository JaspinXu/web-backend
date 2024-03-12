package redlib.backend.model;

import java.util.Date;
import lombok.Data;

/**
 * ����:schedule���ʵ����
 * @version
 * @author:  19826
 * @����ʱ��: 2024-03-12
 */
@Data
public class Schedule {
    /**
     * ����
     */
    private Integer id;

    /**
     * ԤԼ����
     */
    private String reservationCode;

    /**
     * ʵ���ұ���
     */
    private String labCode;

    /**
     * ʵ��������
     */
    private String labName;

    /**
     * ʵ���ʦ����
     */
    private String teacherCode;

    /**
     * ��ʦ����
     */
    private String teacherName;

    /**
     * �γ̱���
     */
    private String courseCode;

    /**
     * �γ�����
     */
    private String courseName;

    /**
     * ѧ������
     */
    private Integer studentNum;

    /**
     * �ܴ�
     */
    private String courseWeek;

    /**
     * ����
     */
    private String courseDay;

    /**
     * �ڴ�
     */
    private String courseTime;

    /**
     * ʵ����ܴ���
     */
    private Integer totalCount;

    /**
     * ��ϵ�绰
     */
    private String contactPhone;

    /**
     * ��ע
     */
    private String description;

    /**
     * ԤԼ��״̬
     */
    private Byte status;

    /**
     * ����ʱ��
     */
    private Date createdAt;

    /**
     * ����ʱ��
     */
    private Date updatedAt;

    /**
     * ������
     */
    private String createdBy;

    /**
     * ������
     */
    private String updatedBy;

    /**
     * ɾ�����
     */
    private Byte deleted;
}