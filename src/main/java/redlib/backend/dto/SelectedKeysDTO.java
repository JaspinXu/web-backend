package redlib.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class SelectedKeysDTO {
    private List<Integer> selectedFreeLabKeys;
}
