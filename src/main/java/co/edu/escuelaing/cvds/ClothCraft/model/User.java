package co.edu.escuelaing.cvds.ClothCraft.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;


import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true)
    private String id;
    
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "username", nullable = false)
    private String username;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wardrobe wardrobe;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Calendary calendary;


    public User(String name, String email, String username, String password,  Wardrobe wardrobe, Calendary calendary) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.wardrobe = wardrobe;
        this.calendary = calendary;
    }
    

	public UserDTO toDTO() {
        return new UserDTO(id, name, email, password, username,
            wardrobe != null ? wardrobe.getId() : null,
            calendary != null ? calendary.getId() : null);
    }
    
    @Override
    public String toString(){
        return toDTO().toString();
    }
}
