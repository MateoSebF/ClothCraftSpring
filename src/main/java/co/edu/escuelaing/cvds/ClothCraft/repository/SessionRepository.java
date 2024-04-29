package co.edu.escuelaing.cvds.ClothCraft.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.escuelaing.cvds.ClothCraft.model.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID>{
    public Session findByToken(UUID token);

    public Session deleteByToken(UUID token);
}