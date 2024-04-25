package co.edu.escuelaing.cvds.ClothCraft.model.DTO;

import lombok.*;
import java.util.List;

import co.edu.escuelaing.cvds.ClothCraft.model.Category;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class OutfitDTO {
    private String id;
    private String name;
    private Category category;
    private List<String> clothesIds;
}
