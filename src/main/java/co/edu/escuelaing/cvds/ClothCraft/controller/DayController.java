package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Calendary;
import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.Day;
import co.edu.escuelaing.cvds.ClothCraft.model.Notification;
import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.ClothingDTO;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.DayDTO;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.OutfitDTO;
import co.edu.escuelaing.cvds.ClothCraft.service.CalendaryService;
import co.edu.escuelaing.cvds.ClothCraft.service.ClothingService;
import co.edu.escuelaing.cvds.ClothCraft.service.DayService;
import co.edu.escuelaing.cvds.ClothCraft.service.NotificationService;
import co.edu.escuelaing.cvds.ClothCraft.service.OutfitService;
import co.edu.escuelaing.cvds.ClothCraft.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Class that handles the day controller
 */
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
    @Autowired
    private NotificationService notificationService;

    /*
     * Method that gets the day by id
     * 
     * @param id
     * 
     * @return ResponseEntity<DayDTO>
     */
    @GetMapping("/{date}")
    public ResponseEntity<List<ClothingDTO>> getClothingByOutfitAndDate(
            @RequestAttribute("userId") String userId, @PathVariable Date date) {
        User user = userService.getUserById(userId);
        if (user != null) {
            Day day = user.getCalendary().getDays().stream()
                    .filter(d -> d.getDate().equals(date))
                    .findFirst()
                    .orElse(null);
            if (day != null && day.getOutfit() != null) {
                OutfitDTO outfitDTO = day.getOutfit().toDTO();
                List<String> clothingIds = outfitDTO.getClothesIds();
                List<Clothing> clothingList = getClothingByClothingIds(clothingIds);
                List<ClothingDTO> clothingDTOList = clothingList.stream()
                        .map(Clothing::toDTO)
                        .collect(Collectors.toList());
                return new ResponseEntity<>(clothingDTOList, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that gets the day by id
     * 
     * @param id
     * 
     * @return ResponseEntity<DayDTO>
     */
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

    /*
     * Method that gets the days
     * 
     * @return ResponseEntity<List<DayDTO>>, the list of all the days
     */
    @GetMapping("/all")
    public ResponseEntity<List<DayDTO>> getAllDays(@RequestAttribute("userId") String userId) {
        User user = userService.getUserById(userId);
        Calendary calendary = user.getCalendary();
        List<Day> days = calendary.getDays();
        List<DayDTO> dayDTOs = days.stream()
                .map(Day::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dayDTOs, HttpStatus.OK);
    }

    @GetMapping("/all/all")
    public ResponseEntity<List<DayDTO>> getAllDays() {
        List<Day> days = dayService.getAllDays();
        List<DayDTO> dayDTOs = days.stream()
                .map(Day::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dayDTOs, HttpStatus.OK);
    }
    
    /*
     * Method that creates a day for a specific user
     * 
     * @param userId, the id of the user to create the day
     * 
     * @param dayDTO, the day to create
     * 
     * @return ResponseEntity<DayDTO>, the created day
     */
    @PostMapping("/user")
    public ResponseEntity<DayDTO> createOutfitForUserAndDay(
            @RequestAttribute("userId") String userId,
            @RequestBody DayDTO dayDTO) {
        User user = userService.getUserById(userId);
        if (user != null) {
            Day day = convertToObject(dayDTO);
            day.setCalendary(user.getCalendary());
            day = dayService.createDay(day);
            if (day != null){
                Notification notification = new Notification();
                notification.setDay(day);
                notification.setUser(user);
                notification.setOutfit(day.getOutfit());
                notificationService.saveNotification(notification);
            }
            return new ResponseEntity<>(day.toDTO(), HttpStatus.CREATED);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that updates the day
     * 
     * @param id, the id of the day to update
     * 
     * @param dayDTO, the day to update
     * 
     * @return ResponseEntity<DayDTO>, the updated day
     */
    @PutMapping("/{id}")
    public ResponseEntity<DayDTO> updateDay(@PathVariable String id, @RequestBody DayDTO dayDTO) {
        Day updatedDay = dayService.updateDay(id, convertToObject(dayDTO));
        if (updatedDay != null)
            return new ResponseEntity<>(updatedDay.toDTO(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that deletes the day
     * 
     * @param id, the id of the day to delete
     * 
     * @return ResponseEntity<Void>, the status of the deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDay(@PathVariable String id) {
        boolean deleted = dayService.deleteDay(id);
        if (deleted)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that converts the dayDTO to a day object
     * 
     * @param dayDTO, the dayDTO to convert
     * 
     * @return Day, the day object
     */
    private Day convertToObject(DayDTO dayDTO) {
        Calendary calendary = dayDTO.getCalendaryId() != null
                ? calendaryService.getCalendaryById(dayDTO.getCalendaryId())
                : null;
        Outfit outfit = dayDTO.getOutfitId() != null ? outfitService.getOutfitById(dayDTO.getOutfitId()) : null;
        Day day = dayDTO.toEntity(calendary, outfit);
        return day;
    }

}
