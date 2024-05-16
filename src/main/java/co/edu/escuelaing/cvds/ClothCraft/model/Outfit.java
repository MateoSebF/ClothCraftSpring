package co.edu.escuelaing.cvds.ClothCraft.model;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.OutfitDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Outfit")
/**
 * Outfit
 */
public class Outfit {
    @Id 
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
   @Column(name = "id", nullable = false, unique = true)
    private String id;
    
    @Column(name = "name")
    private String name;

    @Column(name =  "category")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "wardrobe_id")
    private Wardrobe wardrobe;

    @ManyToMany
    @JoinTable(name = "Outfit_Clothing", 
    joinColumns = @JoinColumn(name = "outfit_id"),
    inverseJoinColumns = @JoinColumn(name = "clothing_id"))
    private List<Clothing> clothes;
    
	public OutfitDTO toDTO() {
        List<String> clothesIds = clothes.stream()
                                        .map(Clothing::getId)
                                        .collect(Collectors.toList());

        return new OutfitDTO(id, name, category, wardrobe != null ? wardrobe.getId() : null, clothesIds);
    }
    @Override
    public String toString(){
        return toDTO().toString();
    }
}