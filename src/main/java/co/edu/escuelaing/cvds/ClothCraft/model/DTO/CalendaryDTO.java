package co.edu.escuelaing.cvds.ClothCraft.model.DTO;

import lombok.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CalendaryDTO {
    private String id;
    private String userId;
    private List<String> dayIds;
}
