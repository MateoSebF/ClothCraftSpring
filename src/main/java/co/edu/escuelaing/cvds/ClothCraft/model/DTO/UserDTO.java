package co.edu.escuelaing.cvds.ClothCraft.model.DTO;

import co.edu.escuelaing.cvds.ClothCraft.model.Calendary;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;
import lombok.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private String username;
    private String photoProfile;
    private String wardrobeId;
    private String calendaryId;

    public User toEntity(Wardrobe wardrobe, Calendary calendary) {
        String hashedPassword = hashPassword(password);

        User user = new User(id, name, email, hashedPassword, username, photoProfile, wardrobe, calendary);

        return user;
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 32) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public String getWardrobeId() {
        return wardrobeId;
    }

    public String getCalendaryId() {
        return calendaryId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserDTO) {
            UserDTO user = (UserDTO) obj;
            return (this.id == null ||user.getId() == null) ? this.id == null : user.getId().equals(this.id)
                && (this.name == null ||user.getName() == null) ? this.name == null : user.getName().equals(this.name)
                && (this.email == null ||user.getEmail() == null) ? this.email == null : user.getEmail().equals(this.email)
                && (this.password == null ||user.getPassword() == null) ? this.password == null : user.getPassword().equals(this.password)
                && (this.username == null ||user.getUsername() == null) ? this.username == null : user.getUsername().equals(this.username)
                && (this.photoProfile == null ||user.getPhotoProfile() == null) ? this.photoProfile == null : user.getPhotoProfile().equals(this.photoProfile)
                && (this.wardrobeId == null ||user.getWardrobeId() == null) ? this.wardrobeId == null : user.getWardrobeId().equals(this.wardrobeId)
                && (this.calendaryId == null ||user.getCalendaryId() == null) ? this.calendaryId == null : user.getCalendaryId().equals(this.calendaryId);
            
        }
        return false;
    }
}
