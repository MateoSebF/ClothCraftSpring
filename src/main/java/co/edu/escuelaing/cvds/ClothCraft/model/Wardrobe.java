package co.edu.escuelaing.cvds.ClothCraft.model;

import jakarta.persistence.CascadeType;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.*;
import lombok.*;

import co.edu.escuelaing.cvds.ClothCraft.model.User;

/**
 * User
 */
@SuppressWarnings("unused")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Wardrobe")
public class Wardrobe {
    @Id
    @Column(name = "id", nullable = false,  unique =     true)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToMany
    private Set<Clothing> clothes;

    @Override
    public String toString(){
        return "Wardrobe{" + "id='" + id + '\'' + ", user_id='" + user + '\'' +'}';
    }
}
