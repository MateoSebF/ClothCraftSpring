package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Calendary;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.UserDTO;
import co.edu.escuelaing.cvds.ClothCraft.service.CalendaryService;
import co.edu.escuelaing.cvds.ClothCraft.service.UserService;
import co.edu.escuelaing.cvds.ClothCraft.service.WardrobeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

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
        User user = userService.getUserById(uniqueKey);
        if (user == null) user = userService.getUserByEmail(uniqueKey);
        if (user == null) user = userService.getUserByUserName(uniqueKey);
        if (user != null) response = new ResponseEntity<>(user.toDTO(), HttpStatus.OK);
        return response;
    }
    @GetMapping("/photoProfile/{uniqueKey}")
    public ResponseEntity<byte[]> getPhotoProfileByUniqueKey(@PathVariable String uniqueKey) {
        ResponseEntity<byte[]> response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        byte[] photoProfile;
        User user = userService.getUserById(uniqueKey);
        if (user == null) user = userService.getUserByEmail(uniqueKey);
        if (user == null) user = userService.getUserByUserName(uniqueKey);
        if (user != null) {
            photoProfile = user.getPhotoProfile();
            response = ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(photoProfile);
        }
        return response;
    }
    @GetMapping("/id/photoProfile/{id}")
    public ResponseEntity<byte[]> getPhotoProfileById(@PathVariable String id) {
        User user = userService.getUserById(id);
        if (user != null) {
            byte[] photoProfile = user.getPhotoProfile();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(photoProfile);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/email/photoProfile/{email}")
    public ResponseEntity<byte[]> getPhotoProfileByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user != null) {
            byte[] photoProfile = user.getPhotoProfile();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(photoProfile);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/username/photoProfile/{username}")
    public ResponseEntity<byte[]> getPhotoProfileByUserName(@PathVariable String username) {
        User user = userService.getUserByUserName(username);
        if (user != null) {
            byte[] photoProfile = user.getPhotoProfile();
            return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(photoProfile);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        List<UserDTO> userDTOList = userList.stream()
                .map(User::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) throws IOException {
        String imagePath = "images/profile.png";
        byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
        userDTO.setPhotoProfile(imageBytes);
        User user = userService.createUser(convertToObject(userDTO));
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
        Wardrobe wardrobe = userDTO.getWardrobeId() != null ? wardrobeService.getWardrobeById(userDTO.getWardrobeId()) : null;
        Calendary calendary = userDTO.getCalendaryId() != null ? calendaryService.getCalendaryById(userDTO.getCalendaryId()) : null;
        return userDTO.toEntity(wardrobe,calendary);
    }
}
