package redlib.backend.model;

import java.util.Date;
import lombok.Data;

/**
 * ����:semester���ʵ����
 * @version
 * @author:  19826
 * @����ʱ��: 2024-03-20
 */
@Data
public class Semester {
    /**
     * 
     */
    private Integer id;

    /**
     * ѧ����
     */
    private String semesterName;

    /**
     * ����ʱ��
     */
    private Date createdAt;

    /**
     * ����ʱ��
     */
    private Date updatedAt;
}