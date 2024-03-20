package redlib.backend.model;

import java.util.Date;
import lombok.Data;

/**
 * 描述:semester表的实体类
 * @version
 * @author:  19826
 * @创建时间: 2024-03-20
 */
@Data
public class Semester {
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
}