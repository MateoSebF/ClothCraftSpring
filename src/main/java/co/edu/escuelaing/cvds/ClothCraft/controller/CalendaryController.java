package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Calendary;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.CalendaryDTO;
import co.edu.escuelaing.cvds.ClothCraft.service.CalendaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/calendary")
public class CalendaryController {

    @Autowired
    private CalendaryService calendaryService;

    @GetMapping("/{id}")
    public ResponseEntity<CalendaryDTO> getCalendaryById(@PathVariable String id) {
        Calendary calendary = calendaryService.getCalendaryById(id);
        if (calendary != null) {
            return new ResponseEntity<>(calendary.toDTO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<CalendaryDTO>> getAllCalendary() {
        List<Calendary> calendaryList = calendaryService.getAllCalendary();
        List<CalendaryDTO> calendaryDTOList = calendaryList.stream()
                .map(Calendary::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(calendaryDTOList, HttpStatus.OK);
    }

    /* 
    @PostMapping
    public ResponseEntity<CalendaryDTO> createCalendary(@RequestBody CalendaryDTO calendaryDTO) {
        Calendary calendary = calendaryService.createCalendary(calendaryDTO.toEntity());
        if (calendary != null) {
            return new ResponseEntity<>(calendary.toDTO(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CalendaryDTO> updateCalendary(@PathVariable String id, @RequestBody CalendaryDTO calendaryDTO) {
        Calendary updatedCalendary = calendaryService.updateCalendary(id, calendaryDTO.toEntity());
        if (updatedCalendary != null) {
            return new ResponseEntity<>(updatedCalendary.toDTO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCalendary(@PathVariable String id) {
        boolean deleted = calendaryService.deleteCalendary(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
