package co.edu.escuelaing.cvds.ClothCraft.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


import org.hibernate.annotations.GenericGenerator;

import co.edu.escuelaing.cvds.ClothCraft.model.DTO.UserDTO;


/**
 * Represents a User in the ClothCraft application.
 * 
 * This class contains information about a user, such as their name, email, password, username, photo profile, wardrobe, and calendary.
 * It also provides methods to convert the User object to a UserDTO object and to retrieve a string representation of the User object.
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
    @Column(name = "photo", nullable = false, length = 1048576)
    private String photoProfile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Wardrobe wardrobe;
    
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Calendary calendary;

    public User(String name, String email, String password, 
                String username, String photoProfile,
                Wardrobe wardrobe, Calendary calendary) {

        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.username = username;
        try {
            String imageUrl = "https://cdn-icons-png.flaticon.com/512/1361/1361728.png";
            URI uri = new URI(imageUrl);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (InputStream inputStream = uri.toURL().openStream()) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                try {
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            byte[] imageBytes = outputStream.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            this.photoProfile = base64Image;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.wardrobe = wardrobe;
        this.calendary = calendary;
    }
    
    public User(String email, String password) {
        this.email = email;
        this.password = password;
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

    public Set<Clothing> getAllClothing() {
        return wardrobe.getClothes();
    }
    public Set<Outfit> getAllOutfits() {
        return wardrobe.getOutfits();
    }

    public List<Clothing> getAllClothingByType(String type) {
        return wardrobe.getAllClothingByType(type);
    }

    public int getNumClothing() {
        return wardrobe.getNumClothing();
    }

    public int getNumOutfits() {
        return wardrobe.getNumOutfits();
    }

    public Set<Clothing> getAllLikedClothing() {
        return wardrobe.getLiked();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User user = (User) obj;
            return user.toDTO().equals(this.toDTO());
        }
        return false;
    }

}
