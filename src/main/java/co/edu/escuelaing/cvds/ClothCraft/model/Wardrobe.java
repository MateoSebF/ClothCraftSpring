package co.edu.escuelaing.cvds.ClothCraft.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.GenericGenerator;
import jakarta.persistence.*;
import lombok.*;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.WardrobeDTO;

/**
 * User
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Wardrobe")
public class Wardrobe {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false,  unique = true)
    private String id;

    @Column(name = "layers")
    private List<ClothingType> layers;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    @ManyToMany
    @JoinTable(name = "Wardrobe_Clothing", 
    joinColumns = @JoinColumn(name = "wardrobe_id"), 
    inverseJoinColumns = @JoinColumn(name = "clothing_id"))
    private Set<Clothing> clothes;

    @ManyToMany
    @JoinTable(name = "Wardrobe_Outfit",
    joinColumns = @JoinColumn(name = "wardrobe_id"),
    inverseJoinColumns = @JoinColumn(name = "outfit_id"))
    private Set<Outfit> outfits;

    /*
     * Constructor used to create a Wardrobe object from a WardrobeDTO object
     * 
     * @param wardrobeDTO the WardrobeDTO object
     * @param user the user that owns the wardrobe
     * @param clothes the clothes that the wardrobe has
     */

    public Wardrobe(String id, User user, Set<Clothing> clothes, Set<Outfit> outfits) {
	      this.id = id;
        this.layers = new ArrayList<>();
        layers.add(ClothingType.SHIRT);
        layers.add(ClothingType.PANTS);
        this.user = user;
        this.clothes = clothes;
        this.outfits = outfits; 
    }

    /*
     * Constructor used to create a Wardrobe object from a new user
     * 
     * @param user the user that owns the wardrobe
     */
    public Wardrobe(User user) {
        this.user = user;
        this.layers = new ArrayList<>();
        layers.add(ClothingType.SHIRT);
        layers.add(ClothingType.PANTS);
        this.clothes = new HashSet<>();
        this.outfits = new HashSet<>();
    }

	public WardrobeDTO toDTO() {
        Set<String> clothesIds = new HashSet<>();
        for (Clothing clothing : clothes) {
            clothesIds.add(clothing.getId());
        }
        Set<String> outfitIds = new HashSet<>();
        for (Outfit outfit : outfits) {
            outfitIds.add(outfit.getId());
        }
        return new WardrobeDTO(id, user.getId(), clothesIds, outfitIds);
    }

    @Override
    public String toString(){
        return toDTO().toString();
    }

    public void addClothing(Clothing clothing) {
        clothes.add(clothing);
    }

    public void addOutfit(Outfit outfit) {
        outfits.add(outfit);
    }

    public List<Clothing> getAllClothingByType(String type) {
        List<Clothing> clothingList = new ArrayList<>();
        ClothingType clothingType = ClothingType.valueOf(type);
        for (Clothing clothing : clothes) {
            try{
                if (clothing.getType().equals(clothingType)) {
                    clothingList.add(clothing);
                }
            } catch (Exception e){
                
            }
            
        }
        return clothingList;
    }

    public int getNumClothing() {
        return clothes.size();
    }

    public int getNumOutfits() {
        return outfits.size();
    }

    public List<ClothingType> getUpperLayers(String layer) {
        ClothingType layerType = ClothingType.valueOf(layer);
        List<ClothingType> upperLayers = new ArrayList<>();
        for (ClothingType clothingType : ClothingType.values()) {
            if (clothingType.ordinal() < layerType.ordinal()) {
                upperLayers.add(clothingType);
            }
        }
        return upperLayers;
    }
    public List<ClothingType> getLowerLayers(String layer) {
        ClothingType layerType = ClothingType.valueOf(layer);
        List<ClothingType> lowerLayers = new ArrayList<>();
        for (ClothingType clothingType : ClothingType.values()) {
            if (clothingType.ordinal() > layerType.ordinal()) {
                lowerLayers.add(clothingType);
            }
        }
        return lowerLayers;
    }

    public void addUpperLayer(String layer) {
        ClothingType layerType = ClothingType.valueOf(layer);
        layers.add(0, layerType);
    }

    public void addLowerLayer(String layer) {
        ClothingType layerType = ClothingType.valueOf(layer);
        layers.add(layerType);
    }

    public void removeLayer(ClothingType valueOf) {
        layers.remove(valueOf);
    }
}
