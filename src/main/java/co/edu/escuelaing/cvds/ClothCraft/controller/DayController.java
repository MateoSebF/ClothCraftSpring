package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Day;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.DayDTO;
import co.edu.escuelaing.cvds.ClothCraft.service.CalendaryService;
import co.edu.escuelaing.cvds.ClothCraft.service.DayService;
import co.edu.escuelaing.cvds.ClothCraft.service.OutfitService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/day")
public class DayController {

    @Autowired
    private DayService dayService;
    @Autowired
    private CalendaryService calendaryService;
    @Autowired
    private OutfitService outfitService;

    @GetMapping("/{id}")
    public ResponseEntity<DayDTO> getDayById(@PathVariable String id) {
        Day day = dayService.getDayById(id);
        if (day != null) {
            return new ResponseEntity<>(day.toDTO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<DayDTO>> getAllDays() {
        List<Day> dayList = dayService.getAllDays();
        List<DayDTO> dayDTOList = dayList.stream()
                .map(Day::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dayDTOList, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<DayDTO> createDay(@RequestBody DayDTO dayDTO) {
        Day day = dayService.createDay(convertToObject(dayDTO));
        if (day != null) {
            return new ResponseEntity<>(day.toDTO(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<DayDTO> updateDay(@PathVariable String id, @RequestBody DayDTO dayDTO) {
        Day updatedDay = dayService.updateDay(id, convertToObject(dayDTO));
        if (updatedDay != null) {
            return new ResponseEntity<>(updatedDay.toDTO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDay(@PathVariable String id) {
        boolean deleted = dayService.deleteDay(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private Day convertToObject(DayDTO dayDTO) {
        Day day = dayDTO.toEntity(calendaryService.getCalendaryById(dayDTO.getId()),outfitService.getOutfitById(dayDTO.getId()));
        return day;
    }

}
