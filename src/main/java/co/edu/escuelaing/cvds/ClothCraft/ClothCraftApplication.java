package co.edu.escuelaing.cvds.ClothCraft;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import co.edu.escuelaing.cvds.ClothCraft.service.ClothingService;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ClothCraftApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ClothCraftApplication.class, args);
	}
	@Autowired
	ClothingService clothingService;

	@Bean
	public CommandLineRunner run() {
		return (args) -> {
			log.info("\nrunning the application...\n");
		};
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
