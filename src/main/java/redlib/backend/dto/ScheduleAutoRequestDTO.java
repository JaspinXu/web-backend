package redlib.backend.dto;

import lombok.Data;
import redlib.backend.dto.query.CheckQueryDTO;
@Data
public class ScheduleAutoRequestDTO {
    private CheckQueryDTO queryDTO;
    private SelectedKeysDTO keysDTO;
}
