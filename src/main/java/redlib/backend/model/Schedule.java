package redlib.backend.model;

import java.util.Date;
import lombok.Data;

/**
 * ����:schedule���ʵ����
 * @version
 * @author:  19826
 * @����ʱ��: 2024-03-20
 */
@Data
public class Schedule {
    /**
     * ����
     */
    private Integer id;

    /**
     * ʵ��������
     */
    private String labName;

    /**
     * �γ�����
     */
    private String courseName;

    /**
     * ��ʦ����
     */
    private String teacherName;

    /**
     * �ڴ�
     */
    private String courseTime;

    /**
     * �ܴ�
     */
    private String courseWeek;

    /**
     * ����
     */
    private String courseDay;

    /**
     * ѧ����
     */
    private String semesterName;

    /**
     * ѧ������
     */
    private Integer studentNum;

    /**
     * ��ϵ�绰
     */
    private String contactPhone;

    /**
     * ��ע
     */
    private String description;

    /**
     * ������
     */
    private String createdBy;

    /**
     * ������
     */
    private String updatedBy;

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