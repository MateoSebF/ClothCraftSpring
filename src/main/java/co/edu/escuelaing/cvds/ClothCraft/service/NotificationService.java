package co.edu.escuelaing.cvds.ClothCraft.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.escuelaing.cvds.ClothCraft.model.Notification;
import co.edu.escuelaing.cvds.ClothCraft.repository.NotificationRepository;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    public List<Notification> getPendingNotifications() {
        LocalDate targetDate = LocalDate.now().plusDays(2);
        return notificationRepository.findNotificationsByDayDate(targetDate);
    }

    public void deleteNotification(Notification notification) {
        notificationRepository.delete(notification);
    }

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }
}
