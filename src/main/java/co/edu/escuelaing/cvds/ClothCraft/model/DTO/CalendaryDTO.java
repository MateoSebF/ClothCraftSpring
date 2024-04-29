package co.edu.escuelaing.cvds.ClothCraft.model.DTO;

import lombok.*;
import java.util.List;

import co.edu.escuelaing.cvds.ClothCraft.model.Calendary;
import co.edu.escuelaing.cvds.ClothCraft.model.Day;
import co.edu.escuelaing.cvds.ClothCraft.model.User;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CalendaryDTO {
    private String id;
    private String userId;
    private List<String> dayIds;
    public Calendary toEntity(User user, List<Day> days) {
        Calendary calendary = new Calendary(id, user, days);
        return calendary;
    }

}
