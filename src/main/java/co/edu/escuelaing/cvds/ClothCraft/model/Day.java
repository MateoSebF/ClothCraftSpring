package co.edu.escuelaing.cvds.ClothCraft.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

import org.hibernate.annotations.GenericGenerator;

import co.edu.escuelaing.cvds.ClothCraft.model.DTO.DayDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Day")
public class Day {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "date", nullable = false)
    private Date date;
    
    @ManyToOne
    @JoinColumn(name = "calendary_id", referencedColumnName = "id")
    private Calendary calendary;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "outfit_id")
    private Outfit outfit;

    public DayDTO toDTO() {
        return new DayDTO(id, date, calendary.getId(), outfit.getId());
    }
    
    


	@Override
    public String toString(){
        return toDTO().toString();
    }
}
