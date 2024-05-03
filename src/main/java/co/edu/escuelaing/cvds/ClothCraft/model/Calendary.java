package co.edu.escuelaing.cvds.ClothCraft.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.CalendaryDTO;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Calendary")
public class Calendary {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "calendary", cascade = CascadeType.ALL)
    private List<Day> days;

    public Calendary(User user) {
        this.user = user;
        this.days = new ArrayList<>();
    }
    
    public CalendaryDTO toDTO() {
        List<String> dayIds = days.stream()
                                 .map(Day::getId)
                                 .collect(Collectors.toList());

        return new CalendaryDTO(id, user.getId(), dayIds);
    }

	@Override
    public String toString(){
        return toDTO().toString();
    }
}
