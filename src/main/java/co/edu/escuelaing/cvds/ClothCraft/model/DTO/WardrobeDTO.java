package co.edu.escuelaing.cvds.ClothCraft.model.DTO;

import java.util.Set;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class WardrobeDTO {
    private String id;
    private String userId;
    private Set<String> clothesIds;
}
