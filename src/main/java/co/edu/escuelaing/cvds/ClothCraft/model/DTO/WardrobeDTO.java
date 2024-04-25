package co.edu.escuelaing.cvds.ClothCraft.model.DTO;

import java.util.Set;

import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class WardrobeDTO{
    private String id;
    private String userId;
    private Set<String> clothesIds;
    
    public Wardrobe toEntity(User user, Set<Clothing> clothings){
        Wardrobe wardrobe = new Wardrobe(id, user, clothings);
        return wardrobe;
    }


}
