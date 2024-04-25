package co.edu.escuelaing.cvds.ClothCraft.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.UserDTO;

/**
 * User
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
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

    public UserDTO toDTO() {
        return new UserDTO(id, name, email, password,
            wardrobe != null ? wardrobe.getId() : null,
            calendary != null ? calendary.getId() : null);
    }
    
    @Override
    public String toString(){
        return toDTO().toString();
    }
}