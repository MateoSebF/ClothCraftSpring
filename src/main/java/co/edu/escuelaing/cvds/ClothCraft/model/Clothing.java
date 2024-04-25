
package co.edu.escuelaing.cvds.ClothCraft.model;



import java.util.Set;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.*;

/**
 * Clothing
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Clothing")
@JsonIdentityInfo(generator =  ObjectIdGenerators.PropertyGenerator.class, property = "id")
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
}