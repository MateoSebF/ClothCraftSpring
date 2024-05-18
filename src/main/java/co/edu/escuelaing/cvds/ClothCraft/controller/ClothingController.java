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
import java.util.Random;

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
            @RequestAttribute("userId") String userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            Clothing clothing = convertToObject(clothingDTO);
            clothing.setOutfits(new ArrayList<>());
            Wardrobe wardrobe = wardrobeService.getWardrobeByUser(user);
            clothing.setWardrobe(wardrobe);
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
    public ResponseEntity<Void> deleteClothing(@PathVariable String id, @RequestAttribute("userId") String userId) {
        User user = userService.getUserById(userId);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Clothing clothing = clothingService.getClothingById(id);
        if (clothing == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        if (clothing.getWardrobe() == user.getWardrobe()) {
            boolean deleted = clothingService.deleteClothing(id);
            if (deleted)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else{
            boolean isLiked = user.getWardrobe().getLiked().contains(clothing);
            if (isLiked){
                user.getWardrobe().getLiked().remove(clothing);
                userService.updateUser(user.getId(), user);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
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
     * Method that gets the clothing by type of a specific user
     * 
     * @param type, the type of the clothing to get(SHIRT, PANTS, SHOES, HAT, SCARF,
     * ACCESSORIES, OTHER)
     * 
     * @return ResponseEntity<List<ClothingDTO>>, the list of clothing with the type
     */
    @GetMapping("/byType/{type}")
    public ResponseEntity<List<ClothingDTO>> getClothingByType(@PathVariable String type,
            @RequestAttribute("userId") String userId) {
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

    @GetMapping("/randomNonLiked/byType/{type}")
    public ResponseEntity<ClothingDTO> getRandomNonLikedClothing(@RequestAttribute("userId") String userId,
            @PathVariable String type) {
        System.out.println("Getting random non liked clothing of type " + type);
        User user = userService.getUserById(userId);
        if (user != null) {
            Wardrobe wardrobe = user.getWardrobe();
            if (wardrobe != null) {
                Set<String> MyClothingIds = wardrobe.getClothes().stream().map(Clothing::getId)
                        .collect(Collectors.toSet());
                Set<String> likedClothingIds = wardrobe.getLiked().stream().map(Clothing::getId)
                        .collect(Collectors.toSet());
                List<Clothing> nonLikedClothing = clothingService.getAllClothing();
                nonLikedClothing = nonLikedClothing.stream().filter(clothing -> !likedClothingIds.contains(clothing.getId()))
                        .collect(Collectors.toList());
                nonLikedClothing = nonLikedClothing.stream().filter(clothing -> !MyClothingIds.contains(clothing.getId()))
                        .collect(Collectors.toList());
                nonLikedClothing = nonLikedClothing.stream()
                        .filter(clothing -> clothing.getType().toString().equals(type)).collect(Collectors.toList());
                if (!nonLikedClothing.isEmpty()) {
                    Clothing randomClothing = nonLikedClothing.get(new Random().nextInt(nonLikedClothing.size()));
                    return new ResponseEntity<>(randomClothing.toDTO(), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/likeClothing")
    public ResponseEntity<String> likeClothing(@RequestBody ClothingDTO clothingDTO, @RequestAttribute("userId") String userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        Wardrobe wardrobe = wardrobeService.getWardrobeByUser(user);
        if (wardrobe == null) {
            return new ResponseEntity<>("Wardrobe not found", HttpStatus.NOT_FOUND);
        }

        Clothing clothing = convertToObject(clothingDTO);
        // Check if the clothing is already liked to prevent duplicates
        if (!wardrobe.getLiked().contains(clothing)) {
            wardrobe.getLiked().add(clothing);
            wardrobeService.updateWardrobe(wardrobe.getId(), wardrobe);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    /*
     * Method that converts a clothingDTO to a clothing
     * 
     * @param clothingDTO, the clothingDTO to convert
     * 
     * @return Clothing, the clothing converted
     */
    private Clothing convertToObject(ClothingDTO clothingDTO) {
        Wardrobe wardrobe = clothingDTO.getWardrobeId() != null
                ? wardrobeService.getWardrobeById(clothingDTO.getWardrobeId())
                : null;
        List<Outfit> outfits = new ArrayList<>();
        for (String clothing : clothingDTO.getOutfitIds())
            outfits.add(outfitService.getOutfitById(clothing));
        Set<Wardrobe> likedBy = new HashSet<>();
        for (String wardrobeId : clothingDTO.getLikedBy())
            likedBy.add(wardrobeService.getWardrobeById(wardrobeId));
        Clothing clothing = clothingDTO.toEntity(wardrobe, outfits,likedBy);
        return clothing;
    }
}
