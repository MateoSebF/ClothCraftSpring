package co.edu.escuelaing.cvds.ClothCraft.repository;

import co.edu.escuelaing.cvds.ClothCraft.model.Day;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface DayRepository extends JpaRepository<Day, String> {

    @Query("SELECT d.outfit.id FROM Day d WHERE d.calendary.user.id = :userId AND d.date = :date")
    String findOutfitIdByUserAndDate(String userId, Date date);
}
