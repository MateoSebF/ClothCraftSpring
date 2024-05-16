package co.edu.escuelaing.cvds.ClothCraft.controller;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

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
import jakarta.transaction.Transactional;

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

/*
 * Class that handles the clothing controller
 */
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

    /*
     * Method that gets the clothing by id
     * 
     * @param id, the id of the clothing to get
     * 
     * @return ResponseEntity<ClothingDTO>, the clothing with the id
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClothingDTO> getClothingById(@PathVariable String id) {
        Clothing clothing = clothingService.getClothingById(id);
        if (clothing != null)
            return new ResponseEntity<>(clothing.toDTO(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that gets the image of the clothing by id
     * 
     * @param id, the id of the clothing to get the image
     * 
     * @return ResponseEntity<String>, the image of the clothing with the id
     */
    @GetMapping("/image/{id}")
    public ResponseEntity<String> getImagen(@PathVariable String id) {
        id = escapeHtml4(id);
        Clothing clothing = clothingService.getClothingById(id);
        if (clothing != null) {
            String image = clothing.getImage();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that gets all the clothing
     * 
     * @return ResponseEntity<List<ClothingDTO>>, the list of all the clothing
     */
    @GetMapping("/all")
    public ResponseEntity<List<ClothingDTO>> getAllClothing() {
        List<Clothing> clothingList = clothingService.getAllClothing();
        List<ClothingDTO> clothingDTOList = clothingList.stream()
                .map(Clothing::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(clothingDTOList, HttpStatus.OK);
    }

    /*
     * Method that gets the clothing by type
     * 
     * @param type, the type of the clothing to get(SHIRT, PANTS, SHOES, HAT, SCARF,
     * ACCESSORIES, OTHER)
     * 
     * @return ResponseEntity<List<ClothingDTO>>, the list of clothing with the type
     */
    @GetMapping("/AllByType/{type}")
    public ResponseEntity<List<ClothingDTO>> getAllClothingByType(@PathVariable String typeC) {
        ClothingType type = ClothingType.valueOf(typeC);
        List<Clothing> clothingList = clothingService.getAllClothingByType(type);
        List<ClothingDTO> clothingDTOList = clothingList.stream()
                .map(Clothing::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(clothingDTOList, HttpStatus.OK);
    }

    /*
     * Method that gets the clothing by type of a specific user
     * 
     * @param type, the type of the clothing to get(SHIRT, PANTS, SHOES, HAT, SCARF,
     * ACCESSORIES, OTHER)
     * 
     * @return ResponseEntity<List<ClothingDTO>>, the list of clothing with the type
     */
    @GetMapping("/byType/{type}")
    public ResponseEntity<List<ClothingDTO>> getClothingByType(@PathVariable String type,
            @RequestParam(name = "userId", required = true) String userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            List<Clothing> clothingList = user.getAllClothingByType(type);
            List<ClothingDTO> clothingDTOList = clothingList.stream()
                    .map(Clothing::toDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(clothingDTOList, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that creates a clothing for a specific user
     * 
     * @param clothingDTO, the clothing to create
     * 
     * @param userId, the id of the user to create the clothing
     * 
     * @return ResponseEntity<ClothingDTO>, the clothing created
     */
    @PostMapping("")
    public ResponseEntity<ClothingDTO> createClothingForUser(@RequestBody ClothingDTO clothingDTO,
            @RequestParam(name = "userId", required = true) String userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            Clothing clothing = convertToObject(clothingDTO);
            clothing.setOutfits(new ArrayList<>());
            Set<Wardrobe> wardrobes = new HashSet<>();
            Wardrobe wardrobe = wardrobeService.getWardrobeByUser(user);
            wardrobes.add(wardrobe);
            clothing.setWardrobes(wardrobes);
            clothing = clothingService.createClothing(clothing);
            wardrobe.addClothing(clothing);
            wardrobeService.updateWardrobe(wardrobe.getId(), wardrobe);
            if (clothing != null)
                return new ResponseEntity<>(clothing.toDTO(), HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that updates the clothing
     * 
     * @param id, the id of the clothing to update
     * 
     * @param clothingDTO, the clothing to update
     * 
     * @return ResponseEntity<ClothingDTO>, the updated clothing
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClothingDTO> updateClothing(@PathVariable String id, @RequestBody ClothingDTO clothingDTO) {
        Clothing updatedClothing = clothingService.updateClothing(id, convertToObject(clothingDTO));
        if (updatedClothing != null)
            return new ResponseEntity<>(updatedClothing.toDTO(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that deletes the clothing
     * 
     * @param id, the id of the clothing to delete
     * 
     * @return ResponseEntity<Void>, the status of the deletion
     */
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClothing(@PathVariable String id, @RequestParam(name = "userId", required = true) String userId) {
        User user = userService.getUserById(userId);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Clothing clothing = clothingService.getClothingById(id);
        if (clothing == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (clothing.getWardrobe() ==  user.getWardrobe()){
            boolean deleted = clothingService.deleteClothing(id);
            if (deleted)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_AUTHORIZED);
        
    }

    /*
     * Method that gets all the clothing types
     * 
     * @return ResponseEntity<List<ClothingType>>, the list of all the clothing
     * types
     */
    @GetMapping("/ClothingsTypes")
    public ResponseEntity<List<ClothingType>> getAllClothingTypes() {
        List<ClothingType> clothingTypes = new ArrayList<>();
        for (ClothingType clothingType : ClothingType.values())
            clothingTypes.add(clothingType);
        return new ResponseEntity<>(clothingTypes, HttpStatus.OK);
    }

    /*
     * Method that converts a clothingDTO to a clothing
     * 
     * @param clothingDTO, the clothingDTO to convert
     * 
     * @return Clothing, the clothing converted
     */
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
