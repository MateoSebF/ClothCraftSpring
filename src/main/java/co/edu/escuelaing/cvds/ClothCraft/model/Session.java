package co.edu.escuelaing.cvds.ClothCraft.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Session")
public class Session {
    @Id
    @Column(name = "token", nullable=false, unique=true)
    private UUID token;

	@Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Session(User user){
        this.token = UUID.randomUUID();
        this.timestamp = Instant.now();
        this.user = user;
    }


    public UUID getToken() {
        return token;
    }
}