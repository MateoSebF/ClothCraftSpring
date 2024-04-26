package co.edu.escuelaing.cvds.ClothCraft.model.DTO;

import lombok.*;

import java.util.List;
import java.util.Set;

import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ClothingDTO {
    private String id;
    private String name;
    private byte[] image;
    private String color;
    private String size;
    private Set<String> wardrobeIds;
    private List<String> outfitIds;
    
    public Clothing toEntity(Set<Wardrobe> wardrobes, List<Outfit> outfits){
        Clothing clothing = new Clothing(id,name,image,color,size,wardrobes,outfits);
        return clothing;
    }
}
