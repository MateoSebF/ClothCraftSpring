package co.edu.escuelaing.cvds.ClothCraft.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

/**
 * User
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
@JsonIdentityInfo(generator =  ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;
    
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wardrobe wardrobe;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Calendary calendary;

    @Override
    public String toString() {
        return "User{" + "id='" + id + '\'' + ", name='" + name + '\'' +
        ", email='" + email + '\'' + ", password='" + password + '\'' +'}';
    }
}