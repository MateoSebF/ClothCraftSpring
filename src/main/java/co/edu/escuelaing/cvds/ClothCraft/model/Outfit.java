package co.edu.escuelaing.cvds.ClothCraft.model;


import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

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
    @Column(name = "id", nullable = false, unique = true)
    private String id;
    
    @Column(name = "name")
    private String name;

    @Column(name =  "category")
    private Category category;

    @ManyToMany
    private Set<Clothing> clothes;
}
