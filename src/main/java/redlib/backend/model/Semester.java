package redlib.backend.model;

import lombok.Data;

/**
 * ����:semester���ʵ����
 * @version
 * @author:  19826
 * @����ʱ��: 2024-03-12
 */
@Data
public class Semester {
    /**
     * 
     */
    private Integer id;

    /**
     * ѧ�ڱ���
     */
    private String semesterCode;

    /**
     * ѧ�ں�
     */
    private String semesterNumber;

    /**
     * ������
     */
    private String semesterWeek;
}