package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Calendary;
import co.edu.escuelaing.cvds.ClothCraft.model.Day;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.CalendaryDTO;
import co.edu.escuelaing.cvds.ClothCraft.service.CalendaryService;
import co.edu.escuelaing.cvds.ClothCraft.service.DayService;
import co.edu.escuelaing.cvds.ClothCraft.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/*
 * Class that handles the calendary controller
 */
@RestController
@RequestMapping("/calendary")
public class CalendaryController {

    @Autowired
    private CalendaryService calendaryService;
    @Autowired
    private UserService userService;
    @Autowired
    private DayService dayService;

    /*
     * Method that gets the calendary by id
     * 
     * @param id, the id of the calendary to get
     * 
     * @return ResponseEntity<CalendaryDTO>, the calendary with the id
     */
    @GetMapping("/{id}")
    public ResponseEntity<CalendaryDTO> getCalendaryById(@PathVariable String id) {
        Calendary calendary = calendaryService.getCalendaryById(id);
        if (calendary != null)
            return new ResponseEntity<>(calendary.toDTO(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method thaht modifies the calendary
     * 
     * @param id, the id of the calendary to modify
     * 
     * @param calendaryDTO, the calendary to modify
     * 
     * @return ResponseEntity<CalendaryDTO>, the modified calendary
     */
    @PutMapping("/{id}")
    public ResponseEntity<CalendaryDTO> updateCalendary(@PathVariable String id,
            @RequestBody CalendaryDTO calendaryDTO) {
        Calendary calendary = convertToObject(calendaryDTO);
        calendary = calendaryService.updateCalendary(id, calendary);
        if (calendary != null)
            return new ResponseEntity<>(calendary.toDTO(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Mehtods that deletes the calendary
     * 
     * @param id, the id of the calendary to delete
     * 
     * @return ResponseEntity<Void>, the status of the deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalendary(@PathVariable String id) {
        boolean deleted = calendaryService.deleteCalendary(id);
        if (deleted)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that converts the calendaryDTO to a calendary object
     * 
     * @param calendaryDTO, the calendaryDTO to convert
     * 
     * @return Calendary, the calendary object
     */
    private Calendary convertToObject(CalendaryDTO calendaryDTO) {
        User user = calendaryDTO.getUserId() != null ? userService.getUserById(calendaryDTO.getUserId()) : null;
        List<Day> days = new ArrayList<>();
        for (String daysId : calendaryDTO.getDayIds())
            days.add(dayService.getDayById(daysId));
        Calendary calendary = calendaryService.createCalendary(calendaryDTO.toEntity(user, days));
        return calendary;
    }
}
