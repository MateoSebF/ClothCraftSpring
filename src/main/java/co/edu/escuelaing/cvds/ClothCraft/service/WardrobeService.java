package co.edu.escuelaing.cvds.ClothCraft.service;

import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;
import co.edu.escuelaing.cvds.ClothCraft.repository.WardrobeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WardrobeService {

    @Autowired
    private WardrobeRepository wardrobeRepository;

    public Wardrobe getWardrobeById(String id) {
        return wardrobeRepository.findById(id).orElse(null);
    }

    public List<Wardrobe> getAllWardrobes() {
        return wardrobeRepository.findAll();
    }

    public Wardrobe createWardrobe(Wardrobe wardrobe) {
        return wardrobeRepository.save(wardrobe);
    }

    public Wardrobe updateWardrobe(String id, Wardrobe wardrobe) {
        Wardrobe existingWardrobe = wardrobeRepository.findById(id).orElse(null);
        if (existingWardrobe != null) {
            wardrobe.setId(existingWardrobe.getId());
            return wardrobeRepository.save(wardrobe);
        } else {
            return null;
        }
    }

    public boolean deleteWardrobe(String id) {
        Wardrobe existingWardrobe = wardrobeRepository.findById(id).orElse(null);
        if (existingWardrobe != null) {
            wardrobeRepository.delete(existingWardrobe);
            return true;
        } else {
            return false;
        }
    }

    public Wardrobe getWardrobeByUser(User user) {
        return wardrobeRepository.findByUser(user).orElse(null);
    }
}
