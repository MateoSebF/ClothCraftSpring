package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.ClothingType;
import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.ClothingDTO;
import co.edu.escuelaing.cvds.ClothCraft.service.ClothingService;
import co.edu.escuelaing.cvds.ClothCraft.service.OutfitService;
import co.edu.escuelaing.cvds.ClothCraft.service.UserService;
import co.edu.escuelaing.cvds.ClothCraft.service.WardrobeService;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clothing")
public class ClothingController {

    @Autowired
    private UserService userService;
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

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImagen(@PathVariable String id) {
        Clothing clothing = clothingService.getClothingById(id);
        if (clothing != null) {
            byte[] image = clothing.getImage();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
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

    @GetMapping("/byType/{userId}/{type}")
    public ResponseEntity<List<ClothingDTO>> getClothingByType(@PathVariable String userId,
    @PathVariable String type) {
        ResponseEntity<List<ClothingDTO>> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        User user = userService.getUserById(userId);
        if (user != null) {
            List<Clothing> clothingList = user.getAllClothingByType(type);
            List<ClothingDTO> clothingDTOList = clothingList.stream()
                    .map(Clothing::toDTO)
                    .collect(Collectors.toList());
            response = new ResponseEntity<>(clothingDTOList, HttpStatus.OK);
        }
        return response;
    }

    @PostMapping("{userId}")
    public ResponseEntity<ClothingDTO> createClothingForUser(@RequestBody ClothingDTO clothingDTO,
            @PathVariable String userId) {
        User user = userService.getUserById(userId);
        ResponseEntity<ClothingDTO> response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (user != null) {
            Clothing clothing = convertToObject(clothingDTO);
            System.out.println(clothing);
            clothing.setOutfits(new ArrayList<>());
            Set<Wardrobe> wardrobes = new HashSet<>();
            Wardrobe wardrobe = wardrobeService.getWardrobeByUser(user);
            wardrobes.add(wardrobe);
            clothing.setWardrobes(wardrobes);
            clothing = clothingService.createClothing(clothing);
            wardrobe.addClothing(clothing);
            wardrobeService.updateWardrobe(wardrobe.getId(), wardrobe);
            if (clothing != null) {
                response = new ResponseEntity<>(clothing.toDTO(), HttpStatus.CREATED);
            }
        }
        return response;
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

    @GetMapping("/ClothingsTypes")
    public ResponseEntity<List<ClothingType>> getAllClothingTypes() {
        List<ClothingType> clothingTypes = new ArrayList<>();
        for (ClothingType clothingType : ClothingType.values())
            clothingTypes.add(clothingType);
        return new ResponseEntity<>(clothingTypes, HttpStatus.OK);
    }

    private Clothing convertToObject(ClothingDTO clothingDTO) {
        HashSet<Wardrobe> wardrobes = new HashSet<>();
        for (String wardrobeId : clothingDTO.getWardrobeIds())
            wardrobes.add(wardrobeService.getWardrobeById(wardrobeId));
        ArrayList<Outfit> outfits = new ArrayList<>();
        for (String clothing : clothingDTO.getOutfitIds())
            outfits.add(outfitService.getOutfitById(clothing));
        Clothing clothing = clothingDTO.toEntity(wardrobes, outfits);
        return clothing;
    }
}
