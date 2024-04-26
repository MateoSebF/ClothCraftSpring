package co.edu.escuelaing.cvds.ClothCraft.model;

import java.util.HashSet;
import java.util.Set;
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
    @Column(name = "id", nullable = false,  unique = true)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany
    @JoinTable(name = "Wardrobe_Clothing", 
    joinColumns = @JoinColumn(name = "wardrobe_id"), 
    inverseJoinColumns = @JoinColumn(name = "clothing_id"))
    private Set<Clothing> clothes;

    

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
}
