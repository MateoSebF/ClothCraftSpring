package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Calendary;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.UserDTO;
import co.edu.escuelaing.cvds.ClothCraft.service.CalendaryService;
import co.edu.escuelaing.cvds.ClothCraft.service.EmailService;
import co.edu.escuelaing.cvds.ClothCraft.service.UserService;
import co.edu.escuelaing.cvds.ClothCraft.service.WardrobeService;
import jakarta.servlet.http.HttpServletResponse;
import co.edu.escuelaing.cvds.ClothCraft.service.SessionService;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/*
 * The class UserController is a controller that allows to manage the users
 */
/**
 * The UserController class handles the HTTP requests related to user management.
 * It provides endpoints for creating, updating, and retrieving user information.
 * The class also handles user verification and profile management.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final WardrobeService wardrobeService;

    private final CalendaryService calendaryService;

    private final SessionService sessionService;

    private final EmailService emailService;

    public UserController(UserService userService, WardrobeService wardrobeService, CalendaryService calendaryService,
            SessionService sessionService, EmailService emailService) {
        this.userService = userService;
        this.wardrobeService = wardrobeService;
        this.calendaryService = calendaryService;
        this.sessionService = sessionService;
        this.emailService = emailService;
    }

    /*
     * Method used to create a new user assigning a new wardrobe and a calendary
     * 
     * @param userDTO the user to be created
     */
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        userDTO.setName(escapeHtml4(userDTO.getName()));
        userDTO.setEmail(escapeHtml4(userDTO.getEmail()));
        userDTO.setPassword(escapeHtml4(userDTO.getPassword()));
        userDTO.setUsername(escapeHtml4(userDTO.getUsername()));
        try {
            userDTO.setVerified(false);
            User user = convertToObject(userDTO);
            // Create a wardrobe and a calendary for the user
            Wardrobe wardrobe = new Wardrobe(user);
            Calendary calendary = new Calendary(user);
            // Save the user with an initial null wardrobe and calendary
            user = userService.createUser(user);
            // Save the wardrobe and the calendary
            wardrobeService.createWardrobe(wardrobe);
            calendaryService.createCalendary(calendary);
            // Update the user with the wardrobe and the calendary
            user.setWardrobe(wardrobe);
            user.setCalendary(calendary);
            // Save the user with the wardrobe and the calendary
            user = userService.updateUser(user.getId(), user);
            // Send an email to the user to verify the account
            emailService.sendVerificationEmail(user.getEmail(), user.getId());
            return new ResponseEntity<>(escapeHtml4(user.toString()), HttpStatus.CREATED);
        } catch (Exception e) {
            String errorMessage = "An error occurred while processing the request: " + e.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/verify")
    public void verifyAccount(HttpServletResponse response, @RequestParam String token) throws IOException {
        User user = userService.getUserById(token);

        if (user == null) {
            return;
        }

        user.setVerified(true);
        userService.updateUser(token, user);
        response.sendRedirect("https://mango-cliff-06b900910.5.azurestaticapps.net/login");
    }

    /*
     * Method used to get a user by id
     * 
     * @param id the id of the user
     * 
     * @return ResponseEntity<UserDTO>
     */
    @GetMapping("/id")
    public ResponseEntity<UserDTO> getUserById(@RequestAttribute("userId") String userId) {
        User user = userService.getUserById(userId);
        if (user != null)
            return new ResponseEntity<>(user.toDTO(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method used to get the profile of a user
     * 
     * @param userId the id of the user
     * 
     * @return ResponseEntity<Map<String,Object>>
     */
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfileOfAUser(
            @RequestAttribute("userId") String userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            String name = user.getName();
            String userName = user.getUsername();
            String profilePhoto = user.getPhotoProfile();
            int numItems = user.getNumClothing();
            int numOutfits = user.getNumOutfits();
            Map<String, Object> userData = new HashMap<>();
            userData.put("name", name);
            userData.put("username", userName);
            userData.put("profileImage", profilePhoto);
            userData.put("numItems", numItems);
            userData.put("numOutfits", numOutfits);
            return new ResponseEntity<>(userData, HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method used to get a user by unique key
     * 
     * @param uniqueKey the unique key of the user
     * 
     * @return ResponseEntity<UserDTO>
     */
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

    /*
     * Method used to get all the users
     * 
     * @return ResponseEntity<List<UserDTO>>
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        List<UserDTO> userDTOList = userList.stream()
                .map(User::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDTOList, HttpStatus.OK);
    }

    /*
     * Method used to update a user
     * 
     * @param id the id of the user
     * 
     * @param userDTO the user to be updated
     * 
     * @return ResponseEntity<UserDTO>
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable String id, @RequestBody UserDTO userDTO) {
        User updatedUser = userService.updateUser(id, convertToObject(userDTO));
        if (updatedUser != null)
            return new ResponseEntity<>(updatedUser.toDTO(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method used to delete a user
     * 
     * @param id the id of the user
     * 
     * @return ResponseEntity<Void>
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id, @CookieValue("authToken") String authToken) {
        UUID token = UUID.fromString(authToken);
        boolean deletedSession = sessionService.deleteSession(token);
        if (deletedSession) {
            boolean deleted = userService.deleteUser(id);
            if (deleted)
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    /*
     * Method used to update the photo profile of a user
     * 
     * @param userId the id of the user
     * 
     * @param userDTO the user to be updated
     * 
     * @return ResponseEntity<String>
     */
    @PatchMapping("/photo")
    public ResponseEntity<String> updatePhotoProfile(@RequestAttribute("userId") String userId,
            @RequestBody UserDTO userDTO) {
        userId = escapeHtml4(userId);
        userDTO.setPhotoProfile(escapeHtml4(userDTO.getPhotoProfile()));
        try {
            String photo = userDTO.getPhotoProfile();
            photo = escapeHtml4(photo);
            User user = userService.getUserById(userId);
            if (user != null) {
                if (photo.startsWith("data:image/png;base64,")) {
                    photo = photo.substring("data:image/png;base64,".length());
                }
                user.setPhotoProfile(photo);
                userService.updateUser(userId, user);
                return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(photo);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Capturar cualquier excepción y devolver un ResponseEntity con un mensaje de
            // error
            String errorMessage = "An error occurred while updating the photo profile: " + e.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
     * Method used to convert a UserDTO to a User
     * 
     * @param userDTO the user to be converted
     * 
     * @return User
     */
    private User convertToObject(UserDTO userDTO) {
        Wardrobe wardrobe = userDTO.getWardrobeId() != null ? wardrobeService.getWardrobeById(userDTO.getWardrobeId())
                : null;
        Calendary calendary = userDTO.getCalendaryId() != null
                ? calendaryService.getCalendaryById(userDTO.getCalendaryId())
                : null;
        return userDTO.toEntity(wardrobe, calendary);
    }
}
