package co.edu.escuelaing.cvds.ClothCraft.model.DTO;

import lombok.*;

import java.util.List;

import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.ClothingType;
import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClothingDTO {
    private String id;
    private String name;
    private String image;
    private String color;
    private String size;
    private ClothingType type;
    private String wardrobeId;
    private List<String> outfitIds;
    
    public Clothing toEntity(Wardrobe wardrobe, List<Outfit> outfits){
        Clothing clothing = new Clothing(id,name,image,color,size,type,wardrobe,outfits);
        return clothing;
    }
}
