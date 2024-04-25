package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;

import co.edu.escuelaing.cvds.ClothCraft.model.DTO.ClothingDTO;
import co.edu.escuelaing.cvds.ClothCraft.service.ClothingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clothing")
public class ClothingController {

    @Autowired
    private ClothingService clothingService;

    @GetMapping("/{id}")
    public ResponseEntity<ClothingDTO> getClothingById(@PathVariable String id) {
        Clothing clothing = clothingService.getClothingById(id);
        if (clothing != null) {
            return new ResponseEntity<>(clothing.toDTO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClothingDTO>> getAllClothing() {
        List<Clothing> clothingList = clothingService.getAllClothing();
        List<ClothingDTO> clothingDTOList = clothingList.stream()
                .map(Clothing::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(clothingDTOList, HttpStatus.OK);
    }

    /* 
    @PostMapping
    public ResponseEntity<ClothingDTO> createClothing(@RequestBody ClothingDTO clothingDTO) {
        Clothing clothing = clothingService.createClothing(clothingDTO.toEntity());
        if (clothing != null) {
            return new ResponseEntity<>(clothing.toDTO(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClothingDTO> updateClothing(@PathVariable String id, @RequestBody ClothingDTO clothingDTO) {
        Clothing updatedClothing = clothingService.updateClothing(id, clothingDTO.toEntity());
        if (updatedClothing != null) {
            return new ResponseEntity<>(updatedClothing.toDTO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClothing(@PathVariable String id) {
        boolean deleted = clothingService.deleteClothing(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
