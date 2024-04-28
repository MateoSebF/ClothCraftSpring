package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.OutfitDTO;
import co.edu.escuelaing.cvds.ClothCraft.service.ClothingService;
import co.edu.escuelaing.cvds.ClothCraft.service.OutfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/outfit")
public class OutfitController {

    @Autowired
    private OutfitService outfitService;
    @Autowired
    private ClothingService clothingService;

    @GetMapping("/{id}")
    public ResponseEntity<OutfitDTO> getOutfitById(@PathVariable String id) {
        Outfit outfit = outfitService.getOutfitById(id);
        if (outfit != null) {
            return new ResponseEntity<>(outfit.toDTO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<OutfitDTO>> getAllOutfits() {
        List<Outfit> outfitList = outfitService.getAllOutfits();
        List<OutfitDTO> outfitDTOList = outfitList.stream()
                .map(Outfit::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(outfitDTOList, HttpStatus.OK);
    }
    
    @PostMapping
    public ResponseEntity<OutfitDTO> createOutfit(@RequestBody OutfitDTO outfitDTO) {
        Outfit outfit = outfitService.createOutfit(convertToObject(outfitDTO));
        if (outfit != null) {
            return new ResponseEntity<>(outfit.toDTO(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OutfitDTO> updateOutfit(@PathVariable String id, @RequestBody OutfitDTO outfitDTO) {
        Outfit updatedOutfit = outfitService.updateOutfit(id, convertToObject(outfitDTO));
        if (updatedOutfit != null) {
            return new ResponseEntity<>(updatedOutfit.toDTO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOutfit(@PathVariable String id) {
        boolean deleted = outfitService.deleteOutfit(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public Outfit getOutfitEntityById(String id) {
        Outfit outfit = outfitService.getOutfitById(id);
        return outfit;
    }

    private Outfit convertToObject(OutfitDTO outfitDTO) {
        List<Clothing> clothings = new ArrayList<>();
        for (String clothingId : outfitDTO.getClothesIds()) clothings.add(clothingService.getClothingById(clothingId));
        Outfit outfit = outfitDTO.toEntity(clothings);
        return outfit;
    }
}
