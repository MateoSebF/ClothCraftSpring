package co.edu.escuelaing.cvds.ClothCraft;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import co.edu.escuelaing.cvds.ClothCraft.model.Day;
import co.edu.escuelaing.cvds.ClothCraft.model.Notification;
import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;
import co.edu.escuelaing.cvds.ClothCraft.service.DayService;
import co.edu.escuelaing.cvds.ClothCraft.service.EmailService;
import co.edu.escuelaing.cvds.ClothCraft.service.NotificationService;
import co.edu.escuelaing.cvds.ClothCraft.service.UserService;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class ClothCraftApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ClothCraftApplication.class, args);
	}
	@Autowired
	UserService userService;

	@Autowired
	DayService dayService;

	@Autowired
	EmailService emailService;

	@Autowired
	NotificationService notificationService;

	@Bean
	public CommandLineRunner run() {
		return (args) -> {
			log.info("\nrunning the application...\n");
		};
	}

	@Scheduled(cron = "0 30 19 * * ?") // Programa la tarea para que se ejecute a las 12 PM todos los días
    public void sendDailyNotifications() {
        try {
            // Obtén todas las notificaciones pendientes
            List<Notification> notifications = notificationService.getPendingNotifications();

            // Envía un correo para cada notificación
            for (Notification notification : notifications) {
                String to = notification.getUser().getEmail();
                Outfit outfit = notification.getOutfit();
                Day day = notification.getDay();
                emailService.sendOutfitNotificationEmail(to, outfit, day);
				notificationService.deleteNotification(notification);
            }

            System.out.println("All notifications sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
