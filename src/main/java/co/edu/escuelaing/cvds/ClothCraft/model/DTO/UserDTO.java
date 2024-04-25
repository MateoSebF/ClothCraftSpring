package co.edu.escuelaing.cvds.ClothCraft.model.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String password;
    private String wardrobeId;
    private String calendaryId;
}