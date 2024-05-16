package co.edu.escuelaing.cvds.ClothCraft.service;

import java.util.*;
import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.ClothingType;
import co.edu.escuelaing.cvds.ClothCraft.repository.ClothingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;


@Service
public class ClothingService {

    @Autowired
    private ClothingRepository clothingRepository;

    @Autowired
    private UserService userService;

    public Optional<Clothing> getRandomClothingFromAnotherUser(String currentUserId) {
        List<User> allUsers = userService.getAllUsers();
        allUsers.removeIf(user -> user.getId().equals(currentUserId));

        if (allUsers.isEmpty()) {
            return Optional.empty();
        }

        Random random = new Random();
        User randomUser = allUsers.get(random.nextInt(allUsers.size()));
        Set<Clothing> clothingSet = randomUser.getAllClothing();

        if (clothingSet.isEmpty()) {
            return Optional.empty();
        }

        Clothing[] clothingArray = clothingSet.toArray(new Clothing[0]);
        return Optional.of(clothingArray[random.nextInt(clothingArray.length)]);
    }

    public Clothing getClothingById(String id) {
        Optional<Clothing> clothingOptional = clothingRepository.findById(id);
        return clothingOptional.orElse(null);
    }

    public List<Clothing> getAllClothing() {
        return clothingRepository.findAll();
    }

    public Clothing createClothing(Clothing clothing) {
        return clothingRepository.save(clothing);
    }

    public List<Clothing> getAllClothingExcludingUser(Set<String> likedClothingIds, String userId) {
        return clothingRepository.findAllByUserIdNotAndIdNotIn(userId, likedClothingIds);
    }



    public Clothing updateClothing(String id, Clothing newClothing) {
        Optional<Clothing> clothingOptional = clothingRepository.findById(id);
        if (clothingOptional.isPresent()) {
            Clothing existingClothing = clothingOptional.get();
            existingClothing.setName(newClothing.getName());
            existingClothing.setColor(newClothing.getColor());
            existingClothing.setSize(newClothing.getSize());
            // Actualizar otras propiedades si es necesario
            return clothingRepository.save(existingClothing);
        } else {
            return null;
        }
    }

    public boolean deleteClothing(String id) {
        Optional<Clothing> clothingOptional = clothingRepository.findById(id);
        if (clothingOptional.isPresent()) {
            clothingRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public List<Clothing> getAllClothingByType(ClothingType type) {
        return clothingRepository.findByType(type);
    }

}
