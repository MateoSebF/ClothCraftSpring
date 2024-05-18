package co.edu.escuelaing.cvds.ClothCraft.model;

import java.util.stream.Collectors;

import org.hibernate.annotations.GenericGenerator;
import java.util.Set;

import java.util.List;
import jakarta.persistence.*;
import lombok.*;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.ClothingDTO;



/**
 * Represents a clothing item in the ClothCraft application.
 * 
 * This class contains information about a clothing item, such as its name, image, color, size, type of clothing and associated wardrobes and outfits.
 * It also provides methods to convert the clothing item to a DTO (Data Transfer Object) and to retrieve its ID.
 */

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Clothing")
public class Clothing {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false,  unique = true)
    private String id;
    
    @Column(name = "name")
    private String name;
    
    @Lob
    @Column(name = "image", nullable = false, length = 1048576)
    private String image;

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "size")
    private String size;

    @Column(name = "type")
    private ClothingType type;
 
    @ManyToOne
    @JoinColumn(name = "wardrobe_id")
    private Wardrobe wardrobe;

    @ManyToMany(mappedBy = "clothes")
    private List<Outfit> outfits;

    @ManyToMany(mappedBy = "liked")
    private Set<Wardrobe> likedBy;
    
    
	public ClothingDTO toDTO() {
        List<String> outfitIds = outfits.stream()
                                       .map(Outfit::getId)
                                       .collect(Collectors.toList());
        Set<String> wardrobeIds = likedBy.stream()
                                       .map(Wardrobe::getId)
                                       .collect(Collectors.toSet());
        return new ClothingDTO(id, name, image, color, size, type , wardrobe != null ? wardrobe.getId() : null, outfitIds,wardrobeIds);
    }
    @Override
    public String toString(){
        return toDTO().toString();
    }

}