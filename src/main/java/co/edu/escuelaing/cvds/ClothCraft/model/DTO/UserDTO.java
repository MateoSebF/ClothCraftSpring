package co.edu.escuelaing.cvds.ClothCraft.model.DTO;

import co.edu.escuelaing.cvds.ClothCraft.model.Calendary;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;
import lombok.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;

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
                && (this.password == null ||user.getPassword() == null) ? this.password == null : new BCryptPasswordEncoder().matches(this.password, user.getPassword())
                && (this.username == null ||user.getUsername() == null) ? this.username == null : user.getUsername().equals(this.username)
                && (this.photoProfile == null ||user.getPhotoProfile() == null) ? this.photoProfile == null : user.getPhotoProfile().equals(this.photoProfile)
                && (this.wardrobeId == null ||user.getWardrobeId() == null) ? this.wardrobeId == null : user.getWardrobeId().equals(this.wardrobeId)
                && (this.calendaryId == null ||user.getCalendaryId() == null) ? this.calendaryId == null : user.getCalendaryId().equals(this.calendaryId);
            
        }
        return false;
    }
}
