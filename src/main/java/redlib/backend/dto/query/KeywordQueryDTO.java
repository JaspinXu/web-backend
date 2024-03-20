package redlib.backend.dto.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class KeywordQueryDTO extends PageQueryDTO {
    private String keyword;
    private String orderBy;
}
