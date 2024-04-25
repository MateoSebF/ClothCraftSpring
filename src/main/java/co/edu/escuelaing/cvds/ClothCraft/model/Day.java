package co.edu.escuelaing.cvds.ClothCraft.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.DayDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Day")
public class Day {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @ManyToOne
    @JoinColumn(name = "calendary_id", referencedColumnName = "id")
    private Calendary calendary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "outfit_id")
    private Outfit outfit;

    public DayDTO toDTO() {
        return new DayDTO(id, calendary.getId(), outfit.getId());
    }

    @Override
    public String toString(){
        return toDTO().toString();
    }
}
