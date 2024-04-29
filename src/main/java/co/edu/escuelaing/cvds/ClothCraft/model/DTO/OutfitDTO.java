package co.edu.escuelaing.cvds.ClothCraft.model.DTO;

import lombok.*;
import java.util.List;

import co.edu.escuelaing.cvds.ClothCraft.model.Category;
import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class OutfitDTO {
    private String id;
    private String name;
    private Category category;
    private List<String> clothesIds;

    public Outfit toEntity(List<Clothing> clothes){
        Outfit outfit = new Outfit(id, name, category, clothes);
        return outfit;
    }
}
