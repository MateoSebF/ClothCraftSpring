package co.edu.escuelaing.cvds.ClothCraft.repository;

import co.edu.escuelaing.cvds.ClothCraft.model.Calendary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendaryRepository extends JpaRepository<Calendary, String> {
}
