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

    @OneToMany(mappedBy = "wardrobe", cascade = CascadeType.ALL)
    private Set<Clothing> clothes;

    @OneToMany(mappedBy = "wardrobe", cascade = CascadeType.ALL)
    private Set<Outfit> outfits;

    @ManyToMany
    @JoinTable(name = "Wardrobe_Liked",
            joinColumns = @JoinColumn(name = "wardrobe_id"),
            inverseJoinColumns = @JoinColumn(name = "clothing_id"))
    private Set<Clothing> liked = new HashSet<>();

    /*
     * Constructor used to create a Wardrobe object from a WardrobeDTO object
     * 
     * @param wardrobeDTO the WardrobeDTO object
     * @param user the user that owns the wardrobe
     * @param clothes the clothes that the wardrobe has
     */

    public Wardrobe(String id, User user, Set<Clothing> clothes, Set<Outfit> outfits, Set<Clothing> liked) {
        this.id = id;
        this.layers = new ArrayList<>();
        layers.add(ClothingType.SHIRT);
        layers.add(ClothingType.PANTS);
        this.user = user;
        this.clothes = clothes;
        this.outfits = outfits;
        this.liked = liked;
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
        this.liked = new HashSet<>();
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
        Set<String> likedIds = new HashSet<>();
        for (Clothing clothing : liked) {
            likedIds.add(clothing.getId());
        }
        return new WardrobeDTO(id, user.getId(), clothesIds, outfitIds, likedIds);
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

    public void addLikedClothing(Clothing clothing) {
        liked.add(clothing);
    }

    public void removeLikedClothing(Clothing clothing) {
        liked.remove(clothing);
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
