package redlib.backend.dto;

import lombok.Data;
import java.util.Date;

@Data
public class SemesterDTO {
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
