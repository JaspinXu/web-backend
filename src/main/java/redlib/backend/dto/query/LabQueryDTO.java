package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LabQueryDTO extends PageQueryDTO {
    /**
     * 实验室名称，模糊匹配
     */
    private String labName;
}