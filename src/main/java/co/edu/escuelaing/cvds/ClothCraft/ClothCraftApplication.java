package co.edu.escuelaing.cvds.ClothCraft;


//import co.edu.escuelaing.cvds.ClothCraft.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import co.edu.escuelaing.cvds.ClothCraft.service.UserService;

//import java.nio.file.*;

@SpringBootApplication
@Slf4j
public class ClothCraftApplication {
	private final UserService userService;

    public ClothCraftApplication(UserService userService){
		this.userService = userService;
    }
	
	public static void main(String[] args) {
		SpringApplication.run(ClothCraftApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() {
		return (args) -> {
			/*String imagePath = "images/profile.png";

			byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
			
			for(User user : userService.getAllUsers()) {
				String userId = user.getId();
				user.setPhotoProfile(imageBytes);
				userService.updateUser(userId,user);
			}*/
			log.info("\nGetting all users....");
			userService.getAllUsers().forEach(System.out::println);
		};
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
