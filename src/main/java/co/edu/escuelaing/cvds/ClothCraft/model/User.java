package co.edu.escuelaing.cvds.ClothCraft.model;

import jakarta.persistence.*;
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

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Lob
    @Column(name = "photo", nullable = false, columnDefinition = "BLOB")
    private byte[] photoProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wardrobe wardrobe;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Calendary calendary;

    public User(String name, String email, String username,
                String password, byte[] photoProfile,
                Wardrobe wardrobe, Calendary calendary) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.photoProfile = photoProfile;
        this.wardrobe = wardrobe;
        this.calendary = calendary;
    }
    

	public UserDTO toDTO() {
        return new UserDTO(id, name, email, password, username,  photoProfile,
            wardrobe != null ? wardrobe.getId() : null,
            calendary != null ? calendary.getId() : null);
    }
    
    @Override
    public String toString(){
        return toDTO().toString();
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
