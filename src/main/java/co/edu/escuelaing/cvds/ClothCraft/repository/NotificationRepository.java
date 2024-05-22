package co.edu.escuelaing.cvds.ClothCraft.repository;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.escuelaing.cvds.ClothCraft.model.Notification;

@Repository
public interface NotificationRepository  extends JpaRepository<Notification, String>{

    @Query("SELECT n FROM Notification n WHERE n.day.date = :targetDate")
    List<Notification> findNotificationsByDayDate(@Param("targetDate") LocalDate targetDate);


}
