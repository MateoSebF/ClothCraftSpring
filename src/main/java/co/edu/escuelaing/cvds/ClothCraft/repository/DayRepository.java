package co.edu.escuelaing.cvds.ClothCraft.repository;
import co.edu.escuelaing.cvds.ClothCraft.model.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayRepository extends JpaRepository<Day, String> {
}
