package co.edu.escuelaing.cvds.ClothCraft.service;

import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.ClothingType;
import co.edu.escuelaing.cvds.ClothCraft.repository.ClothingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClothingService {

    @Autowired
    private ClothingRepository clothingRepository;

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
