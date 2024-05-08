package co.edu.escuelaing.cvds.ClothCraft.service;

import co.edu.escuelaing.cvds.ClothCraft.model.Day;
import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;
import co.edu.escuelaing.cvds.ClothCraft.repository.DayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DayService {

    @Autowired
    private DayRepository dayRepository;

    public String findOutfitIdByUserAndDate(String userId, Date date) {
        return dayRepository.findOutfitIdByUserAndDate(userId, date);
    }

    public Day getDayById(String id) {
        Optional<Day> dayOptional = dayRepository.findById(id);
        return dayOptional.orElse(null);
    }

    public List<Day> getAllDays() {
        return dayRepository.findAll();
    }

    public Day createDay(Day day) {
        return dayRepository.save(day);
    }

    public Day updateDay(String id, Day newDay) {
        Optional<Day> dayOptional = dayRepository.findById(id);
        if (dayOptional.isPresent()) {
            Day existingDay = dayOptional.get();
            existingDay.setCalendary(newDay.getCalendary());
            existingDay.setOutfit(newDay.getOutfit());
            // Actualizar otras propiedades si es necesario
            return dayRepository.save(existingDay);
        } else {
            return null;
        }
    }

    public boolean deleteDay(String id) {
        Optional<Day> dayOptional = dayRepository.findById(id);
        if (dayOptional.isPresent()) {
            dayRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
