package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Category;
import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.OutfitDTO;
import co.edu.escuelaing.cvds.ClothCraft.service.ClothingService;
import co.edu.escuelaing.cvds.ClothCraft.service.OutfitService;
import co.edu.escuelaing.cvds.ClothCraft.service.UserService;
import co.edu.escuelaing.cvds.ClothCraft.service.WardrobeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * The class OutfitController is a controller that allows to manage the outfits
 */
@RestController
@RequestMapping("/outfit")
public class OutfitController {

    @Autowired
    private OutfitService outfitService;
    @Autowired
    private UserService userService;
    @Autowired
    private WardrobeService wardrobeService;
    @Autowired
    private ClothingService clothingService;

    /*
     * Method that gets an outfit by its id
     * 
     * @param id, the id of the outfit
     * 
     * @return ResponseEntity<OutfitDTO>, the outfit with the id
     */
    @GetMapping("/{id}")
    public ResponseEntity<OutfitDTO> getOutfitById(@PathVariable String id) {
        Outfit outfit = outfitService.getOutfitById(id);
        if (outfit != null)
            return new ResponseEntity<>(outfit.toDTO(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that gets all the categories
     * 
     * @return ResponseEntity<List<String>>, the list of all the categories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = new ArrayList<>();
        for (Category category : Category.values())
            categories.add(category.toString());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /*
     * Method that gets all the outfits
     * 
     * @return ResponseEntity<List<OutfitDTO>>, the list of all the outfits
     */
    @GetMapping("/all")
    public ResponseEntity<List<OutfitDTO>> getAllOutfits() {
        List<Outfit> outfitList = outfitService.getAllOutfits();
        List<OutfitDTO> outfitDTOList = outfitList.stream()
                .map(Outfit::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(outfitDTOList, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OutfitDTO> createClothingForUser(@RequestBody OutfitDTO outfitDTO,
            @RequestParam(name = "userId", required = true) String userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            Outfit outfit = convertToObject(outfitDTO);
            Set<Wardrobe> wardrobes = new HashSet<>();
            Wardrobe wardrobe = wardrobeService.getWardrobeByUser(user);
            wardrobes.add(wardrobe);
            outfit.setWardrobe(wardrobe);
            outfit = outfitService.createOutfit(outfit);
            wardrobe.addOutfit(outfit);
            wardrobeService.updateWardrobe(wardrobe.getId(), wardrobe);
            if (outfit != null)
                return new ResponseEntity<>(outfit.toDTO(), HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    /*
     * Method that updates an outfit
     * 
     * @param id, the id of the outfit to be updated
     * 
     * @param outfitDTO, the outfit to be updated
     * 
     * @return ResponseEntity<OutfitDTO>, the updated outfit
     */
    @PutMapping("/{id}")
    public ResponseEntity<OutfitDTO> updateOutfit(@PathVariable String id, @RequestBody OutfitDTO outfitDTO) {
        Outfit updatedOutfit = outfitService.updateOutfit(id, convertToObject(outfitDTO));
        if (updatedOutfit != null)
            return new ResponseEntity<>(updatedOutfit.toDTO(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that deletes an outfit
     * 
     * @param id, the id of the outfit to be deleted
     * 
     * @return ResponseEntity<Void>, the response of the server
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOutfit(@PathVariable String id) {
        boolean deleted = outfitService.deleteOutfit(id);
        if (deleted)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that gets an outfit by its id
     * 
     * @param id, the id of the outfit
     * 
     * @return Outfit, the outfit with the id
     */
    public Outfit getOutfitEntityById(String id) {
        Outfit outfit = outfitService.getOutfitById(id);
        return outfit;
    }

    /*
     * Method that converts an OutfitDTO to an Outfit
     * 
     * @param outfitDTO, the outfit to be converted
     * 
     * @return Outfit, the converted outfit
     */
    private Outfit convertToObject(OutfitDTO outfitDTO) {
        Wardrobe wardrobe =  outfitDTO.getWardrobeId() != null ? wardrobeService.getWardrobeById(outfitDTO.getWardrobeId()) : null;
        List<Clothing> clothings = new ArrayList<>();
        for (String clothingId : outfitDTO.getClothesIds())
            clothings.add(clothingService.getClothingById(clothingId));
        Outfit outfit = outfitDTO.toEntity(wardrobe, clothings);
        return outfit;
    }
}
