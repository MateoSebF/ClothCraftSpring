package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.*;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.ClothingDTO;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.OutfitDTO;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.DayDTO;
import co.edu.escuelaing.cvds.ClothCraft.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
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
    @Autowired
    private UserService userService;
    @Autowired
    private ClothingService clothingService;
    @GetMapping("/{id}")
    public ResponseEntity<DayDTO> getDayById(@PathVariable String id) {
        Day day = dayService.getDayById(id);
        if (day != null) {
            return new ResponseEntity<>(day.toDTO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{userId}/{date}")
    public ResponseEntity<List<ClothingDTO>> getClothingByOutfitAndDate(@PathVariable String userId, @PathVariable Date date) {
        ResponseEntity<List<ClothingDTO>> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        User user = userService.getUserById(userId);

        if (user != null) {
            Day day = user.getCalendary().getDays().stream()
                    .filter(d -> d.getDate().equals(date))
                    .findFirst()
                    .orElse(null);

            if (day != null && day.getOutfit() != null) {
                OutfitDTO outfitDTO = day.getOutfit().toDTO();
                List<String> clothingIds = outfitDTO.getClothesIds();

                // Obtener las prendas correspondientes a los IDs
                List<Clothing> clothingList = getClothingByClothingIds(clothingIds);
                // Convertir las prendas a DTO de prendas
                List<ClothingDTO> clothingDTOList = clothingList.stream()
                        .map(Clothing::toDTO)
                        .collect(Collectors.toList());

                response = new ResponseEntity<>(clothingDTOList, HttpStatus.OK);
            }
        }
        return response;
    }

    private List<Clothing> getClothingByClothingIds(List<String> clothingIds) {
        List<Clothing> clothingList = new ArrayList<>();
        for (String clothingId : clothingIds) {
            Clothing clothing = clothingService.getClothingById(clothingId);
            if (clothing != null) {
                clothingList.add(clothing);
            }
        }
        return clothingList;
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

    @PostMapping("/user/{userId}")
    public ResponseEntity<DayDTO> createOutfitForUserAndDay(@PathVariable String userId,
                                                        @RequestBody DayDTO dayDTO) {
        User user = userService.getUserById(userId);
        if (user != null) {
            Day day = convertToObject(dayDTO);
            day.setCalendary(user.getCalendary());
            day = dayService.createDay(day);
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
        Calendary calendary = dayDTO.getCalendaryId() != null ? calendaryService.getCalendaryById(dayDTO.getCalendaryId()) : null;
        Outfit outfit = dayDTO.getOutfitId() != null ? outfitService.getOutfitById(dayDTO.getOutfitId()) : null;
        Day day = dayDTO.toEntity(calendary,outfit);
        return day;
    }

}
