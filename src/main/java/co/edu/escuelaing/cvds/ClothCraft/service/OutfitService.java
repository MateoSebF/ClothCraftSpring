package co.edu.escuelaing.cvds.ClothCraft.service;

import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;
import co.edu.escuelaing.cvds.ClothCraft.repository.OutfitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OutfitService {

    @Autowired
    private OutfitRepository outfitRepository;

    public Outfit getOutfitById(String id) {
        Optional<Outfit> outfitOptional = outfitRepository.findById(id);
        return outfitOptional.orElse(null);
    }

    public List<Outfit> getAllOutfits() {
        return outfitRepository.findAll();
    }

    public Outfit createOutfit(Outfit outfit) {
        return outfitRepository.save(outfit);
    }

    public Outfit updateOutfit(String id, Outfit newOutfit) {
        Optional<Outfit> outfitOptional = outfitRepository.findById(id);
        if (outfitOptional.isPresent()) {
            Outfit existingOutfit = outfitOptional.get();
            existingOutfit.setName(newOutfit.getName());
            existingOutfit.setCategory(newOutfit.getCategory());
            // Actualizar otras propiedades si es necesario
            return outfitRepository.save(existingOutfit);
        } else {
            return null;
        }
    }

    public boolean deleteOutfit(String id) {
        Optional<Outfit> outfitOptional = outfitRepository.findById(id);
        if (outfitOptional.isPresent()) {
            outfitRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
