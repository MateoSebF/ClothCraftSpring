package co.edu.escuelaing.cvds.ClothCraft.model.DTO;

import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
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
    private String color;
    private String size;
    private Set<String> wardrobeIds;
    private List<String> outfitIds;
    
    public Clothing toEntity(){
        Set<Wardrobe> wardrobes = new HashSet<>();
        
        List<Outfit> outfits = new ArrayList<>();
        Clothing clothing = new Clothing(id,name,color,size,wardrobes,outfits);
        return clothing;
    }
}
