package co.edu.escuelaing.cvds.ClothCraft.model.DTO;

import java.util.Set;

import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class WardrobeDTO {
    private String id;
    private String userId;
    private Set<String> clothesIds;
    private Set<String> outfitsIds;
    private Set<String> likedClothesIds;

    /*
     * Constructor used to create a Wardrobe object from a WardrobeDTO object
     * 
     * @param wardrobeDTO the WardrobeDTO object
     * 
     * @param user the user that owns the wardrobe
     * 
     * @param clothes the clothes that the wardrobe has
     */
    public Wardrobe toEntity(String id, User user, Set<Clothing> clothings, Set<Outfit> outfits, Set<Clothing> likedClothes) {
        Wardrobe wardrobe = new Wardrobe(id, user, clothings, outfits, likedClothes);
        return wardrobe;
    }

}
