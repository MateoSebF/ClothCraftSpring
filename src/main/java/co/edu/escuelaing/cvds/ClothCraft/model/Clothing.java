
package co.edu.escuelaing.cvds.ClothCraft.model;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.List;
import jakarta.persistence.*;
import lombok.*;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.ClothingDTO;

/**
 * Clothing
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Clothing")
public class Clothing {
    @Id
    @Column(name = "id", nullable = false,  unique = true)
    private String id;
    
    @Column(name = "name")
    private String name;
    
    /* 
    @Lob
    @Column(name = "Image", nullable = false)
    private byte[] image;*/

    @Column(name = "color", nullable = false)
    private String color;

    @Column(name = "size")
    private String size;

    @ManyToMany(mappedBy = "clothes")
    private Set<Wardrobe> wardrobe;

    @ManyToMany(mappedBy = "clothes")
    private List<Outfit> outfits;

    public ClothingDTO toDTO() {
        Set<String> wardrobeIds = wardrobe.stream()
                                          .map(Wardrobe::getId)
                                          .collect(Collectors.toSet());

        List<String> outfitIds = outfits.stream()
                                       .map(Outfit::getId)
                                       .collect(Collectors.toList());

        return new ClothingDTO(id, name, color, size, wardrobeIds, outfitIds);
    }
    @Override
    public String toString(){
        return toDTO().toString();
    }
}