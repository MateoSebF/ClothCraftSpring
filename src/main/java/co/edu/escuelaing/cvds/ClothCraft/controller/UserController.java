package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Calendary;
import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.ClothingDTO;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.UserDTO;
import co.edu.escuelaing.cvds.ClothCraft.service.CalendaryService;
import co.edu.escuelaing.cvds.ClothCraft.service.UserService;
import co.edu.escuelaing.cvds.ClothCraft.service.WardrobeService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private WardrobeService wardrobeService;
    @Autowired
    private CalendaryService calendaryService;

    @GetMapping("/id/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user.toDTO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{uniqueKey}")
    public ResponseEntity<UserDTO> getUserByUniqueKey(@PathVariable String uniqueKey) {
        ResponseEntity<UserDTO> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        User user = null;
        if (user == null)
            user = userService.getUserByEmail(uniqueKey);
        if (user == null)
            user = userService.getUserByUserName(uniqueKey);
        if (user != null)
            response = new ResponseEntity<>(user.toDTO(), HttpStatus.OK);
        return response;
    }

    @GetMapping("/photoProfile/{uniqueKey}")
    public ResponseEntity<byte[]> getPhotoProfileByUniqueKey(@PathVariable String uniqueKey) {
        ResponseEntity<byte[]> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        byte[] photoProfile;
        User user = null;
        if (user == null)
            user = userService.getUserByEmail(uniqueKey);
        if (user == null)
            user = userService.getUserByUserName(uniqueKey);
        if (user != null) {
            photoProfile = user.getPhotoProfile();
            response = ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(photoProfile);
        }
        return response;
    }

    @GetMapping("/clothings/{id}")
    public ResponseEntity<List<ClothingDTO>> getClothingsByUniqueKey(@PathVariable String id) {
        ResponseEntity<List<ClothingDTO>> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        User user = userService.getUserById(id);
        if (user != null) {
            Set<Clothing> clothingList = user.getAllClothing();
            List<ClothingDTO> clothingDTOList = clothingList.stream()
                    .map(Clothing::toDTO)
                    .collect(Collectors.toList());
            response = new ResponseEntity<List<ClothingDTO>>(clothingDTOList, HttpStatus.OK);
        }
        return response;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        List<UserDTO> userDTOList = userList.stream()
                .map(User::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    /*
     * Method used to create a new user assigning a new wardrobe and a calendary
     * 
     * @param userDTO the user to be created
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO){
        log.info("Initial userDTO recived" + userDTO.toString());
        try{
            String imagePath = "images/profile.png";
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
            userDTO.setPhotoProfile(imageBytes);
        } catch (Exception e) {
            log.error("Error reading the image" + e.getMessage());
        }
        
        log.info("The image was read" );
        User user = convertToObject(userDTO);
        log.info("The user was converted"+ user.toString());
        // Create a wardrobe and a calendary for the user
        Wardrobe wardrobe = new Wardrobe(user);
        Calendary calendary = new Calendary(user);
        log.info("The wardrobe and the calendary were created"+ wardrobe.toString() + calendary.toString());
        // Save the user with a initial null wardrobe and calendary
        user = userService.createUser(user);
        log.info("The user was saved"+ user.toString());
        // Save the wardrobe and the calendary
        wardrobeService.createWardrobe(wardrobe);
        calendaryService.createCalendary(calendary);
        log.info("The wardrobe and the calendary were saved"+ wardrobe.toString() + calendary.toString());
        // Update the user with the wardrobe and the calendary
        user.setWardrobe(wardrobe);
        user.setCalendary(calendary);
        log.info("The user was updated"+ user.toString());
        // Save the user with the wardrobe and the calendary
        user = userService.updateUser(user.getId(), user);
        log.info("The user was updated"+ user.toString());
        if (user != null) {
            return new ResponseEntity<>(user.toDTO(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateUser(id, convertToObject(userDTO));
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser.toDTO(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private User convertToObject(UserDTO userDTO) {
        Wardrobe wardrobe = userDTO.getWardrobeId() != null ? wardrobeService.getWardrobeById(userDTO.getWardrobeId())
                : null;
        Calendary calendary = userDTO.getCalendaryId() != null
                ? calendaryService.getCalendaryById(userDTO.getCalendaryId())
                : null;
        return userDTO.toEntity(wardrobe, calendary);
    }
}
