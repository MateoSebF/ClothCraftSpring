package co.edu.escuelaing.cvds.ClothCraft.repository;

import co.edu.escuelaing.cvds.ClothCraft.model.Day;
import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface DayRepository extends JpaRepository<Day, String> {

    @Query("SELECT d.outfit FROM Day d WHERE d.calendary.user.id = :userId AND d.date = :date")
    List<Outfit> findOutfitsByUserAndDate(String userId, Date date);
}
