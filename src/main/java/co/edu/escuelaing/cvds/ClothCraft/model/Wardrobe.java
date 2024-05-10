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

    /*
     * Constructor used to create a Wardrobe object from a WardrobeDTO object
     * 
     * @param wardrobeDTO the WardrobeDTO object
     * @param user the user that owns the wardrobe
     * @param clothes the clothes that the wardrobe has
     */

    public Wardrobe(String id, User user, Set<Clothing> clothes) {
	      this.id = id;
        this.layers = new ArrayList<>();
        layers.add(ClothingType.SHIRT);
        layers.add(ClothingType.PANTS);
        this.user = user;
        this.clothes = clothes;
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
    }

	public WardrobeDTO toDTO() {
        Set<String> clothesIds = new HashSet<>();
        for (Clothing clothing : clothes) {
            clothesIds.add(clothing.getId());
        }
        return new WardrobeDTO(id, user.getId(), clothesIds);
    }

    @Override
    public String toString(){
        return toDTO().toString();
    }

    public void addClothing(Clothing clothing) {
        clothes.add(clothing);
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

}
