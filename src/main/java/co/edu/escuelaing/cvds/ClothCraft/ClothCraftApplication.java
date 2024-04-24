package co.edu.escuelaing.cvds.ClothCraft;


import java.util.ArrayList;
import java.util.HashSet;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

import co.edu.escuelaing.cvds.ClothCraft.model.*;
import co.edu.escuelaing.cvds.ClothCraft.repository.ClothingRepository;
import co.edu.escuelaing.cvds.ClothCraft.repository.WardrobeRepository;
import co.edu.escuelaing.cvds.ClothCraft.service.UserService;


@SpringBootApplication
@Slf4j
public class ClothCraftApplication {
	private final UserService userService;
	private final WardrobeRepository wardrobeRepository;
	private final ClothingRepository clothingRepository;

	public ClothCraftApplication(UserService userService, 
	WardrobeRepository wardrobeRepository,
	ClothingRepository clothingRepository){
		this.userService = userService;
		this.wardrobeRepository = wardrobeRepository;
		this.clothingRepository = clothingRepository; 
	}
	
	public static void main(String[] args) {
		SpringApplication.run(ClothCraftApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() {
		return (args) -> {
			log.info("Adding Users....");
			User user1 = new User("1a2b3c4d", "Juan Perez", "juan@example.com", "password123",null,null);
			userService.addUser(user1);
			User user2 = new User("e5f6g7h8", "María García", "maria@example.com", "password123",null,null);
			userService.addUser(user2);
			User user3 = new User("i9j0k1l2", "Carlos Rodríguez", "carlos@example.com", "password123",null,null);
			userService.addUser(user3);

			log.info("Adding Clothes....");
			Clothing clothing1 = new Clothing("1",
			"Camisa",
			"red",
			"XL",
			new HashSet<>(),
			new ArrayList<>());
			clothingRepository.save(clothing1);
			log.info("Adding Wardrobes....");
			HashSet<Clothing> clothes = new HashSet<Clothing>();
			clothes.add(clothing1);
			Wardrobe wardrobe1 = new Wardrobe("1",user1, clothes);
			user1.setWardrobe(wardrobe1);
			userService.updateUser(user1);
			wardrobeRepository.save(wardrobe1);
			Wardrobe wardrobe2 = new Wardrobe("2",user2, clothes);
			user2.setWardrobe(wardrobe2);
			userService.updateUser(user2);
			wardrobeRepository.save(wardrobe2);
			Wardrobe wardrobe3 = new Wardrobe("3",user3,  clothes);
			user3.setWardrobe(wardrobe3);
			userService.updateUser(user3);
			wardrobeRepository.save(wardrobe3);

			HashSet<Wardrobe> wardrobes = new HashSet<Wardrobe>();
			wardrobes.add(wardrobe1);
			wardrobes.add(wardrobe2);
			wardrobes.add(wardrobe3);
			clothing1.setWardrobe(wardrobes);
			clothingRepository.save(clothing1);

			log.info("\nGetting all users....");
			userService.findAll().forEach(user -> System.out.println(user));
		};
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
