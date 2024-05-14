package co.edu.escuelaing.cvds.ClothCraft.model.DTO;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;
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
@ToString
public class UserDTO{
    private String id;
    private String name;
    private String email;
    private String password;
    private String username;
    private String photoProfile;
    private String wardrobeId;
    private String calendaryId;



    public User toEntity(Wardrobe wardrobe, Calendary calendary){
        String hashedPassword = hashPassword(password);

        User user = new User(id,name, email, hashedPassword, username, photoProfile, wardrobe, calendary);

        return user;
    }

    private String hashPassword(String password) {
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

    public void escapeHtml(){
        this.id = escapeHtml4(this.id);
        this.name = escapeHtml4(this.name);
        this.email = escapeHtml4(this.email);
        this.password = escapeHtml4(this.password);
        this.username = escapeHtml4(this.username);
        this.photoProfile = escapeHtml4(this.photoProfile);
        this.wardrobeId = escapeHtml4(this.wardrobeId);
        this.calendaryId = escapeHtml4(this.calendaryId);
    }
}
