package co.edu.escuelaing.cvds.ClothCraft.service;

import co.edu.escuelaing.cvds.ClothCraft.model.Calendary;
import co.edu.escuelaing.cvds.ClothCraft.repository.CalendaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CalendaryService {

    @Autowired
    private CalendaryRepository calendaryRepository;

    public Calendary getCalendaryById(String id) {
        Optional<Calendary> calendaryOptional = calendaryRepository.findById(id);
        return calendaryOptional.orElse(null);
    }

    public List<Calendary> getAllCalendary() {
        return calendaryRepository.findAll();
    }

    public Calendary createCalendary(Calendary calendary) {
        return calendaryRepository.save(calendary);
    }

    public Calendary updateCalendary(String id, Calendary newCalendary) {
        Optional<Calendary> calendaryOptional = calendaryRepository.findById(id);
        if (calendaryOptional.isPresent()) {
            Calendary existingCalendary = calendaryOptional.get();
            existingCalendary.setUser(newCalendary.getUser());
            // Actualizar otras propiedades si es necesario
            return calendaryRepository.save(existingCalendary);
        } else {
            return null;
        }
    }

    public boolean deleteCalendary(String id) {
        Optional<Calendary> calendaryOptional = calendaryRepository.findById(id);
        if (calendaryOptional.isPresent()) {
            calendaryRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
