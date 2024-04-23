package co.edu.escuelaing.cvds.ClothCraft.model;

import jakarta.persistence.CascadeType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

import co.edu.escuelaing.cvds.ClothCraft.model.User;

/**
 * User
 */
@SuppressWarnings("unused")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Wardrobe")
public class Wardrobe {
    @Id
    @Column(name = "id", nullable = false,  unique = true)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;

    @Override
    public String toString(){
        return "Wardrobe id = " + id + ", user_id = " + user.getId();
    }
}
