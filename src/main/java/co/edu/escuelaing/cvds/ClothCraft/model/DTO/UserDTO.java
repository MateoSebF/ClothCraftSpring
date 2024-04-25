package co.edu.escuelaing.cvds.ClothCraft.model.DTO;

import co.edu.escuelaing.cvds.ClothCraft.model.Calendary;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserDTO{
    private String id;
    private String name;
    private String email;
    private String password;
    private String wardrobeId;
    private String calendaryId;

    public User toEntity(Wardrobe wardrobe, Calendary calendary){
        User user = new User(id, name, email, password, wardrobe, calendary);
        return user;
    }
}