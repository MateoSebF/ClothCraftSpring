package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.ClothingDTO;
import co.edu.escuelaing.cvds.ClothCraft.service.ClothingService;
import co.edu.escuelaing.cvds.ClothCraft.service.OutfitService;
import co.edu.escuelaing.cvds.ClothCraft.service.WardrobeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clothing")
public class ClothingController {

    @Autowired
    private ClothingService clothingService;
    @Autowired
    private WardrobeService wardrobeService;
    @Autowired
    private OutfitService outfitService;

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

    
    @PostMapping
    public ResponseEntity<ClothingDTO> createClothing(@RequestBody ClothingDTO clothingDTO) {
        Clothing clothing = clothingService.createClothing(convertToObject(clothingDTO));
        if (clothing != null) {
            return new ResponseEntity<>(clothing.toDTO(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClothingDTO> updateClothing(@PathVariable String id, @RequestBody ClothingDTO clothingDTO) {
        Clothing updatedClothing = clothingService.updateClothing(id, convertToObject(clothingDTO));
        if (updatedClothing != null) {
            return new ResponseEntity<>(updatedClothing.toDTO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClothing(@PathVariable String id) {
        boolean deleted = clothingService.deleteClothing(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private Clothing convertToObject(ClothingDTO clothingDTO){
        HashSet<Wardrobe> wardrobes = new HashSet<>();
        for(String wardrobeId : clothingDTO.getWardrobeIds())wardrobes.add(wardrobeService.getWardrobeById(wardrobeId));
        ArrayList<Outfit> outfits = new ArrayList<>();
        for(String clothing : clothingDTO.getOutfitIds())outfits.add(outfitService.getOutfitById(clothing));
        Clothing clothing = clothingDTO.toEntity(wardrobes, outfits);
        return clothing;
    }
}
