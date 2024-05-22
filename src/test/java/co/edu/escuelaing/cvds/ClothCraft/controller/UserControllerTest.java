package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.DTO.UserDTO;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;
import co.edu.escuelaing.cvds.ClothCraft.model.Calendary;
import co.edu.escuelaing.cvds.ClothCraft.service.UserService;
import co.edu.escuelaing.cvds.ClothCraft.service.WardrobeService;
import co.edu.escuelaing.cvds.ClothCraft.service.CalendaryService;
import co.edu.escuelaing.cvds.ClothCraft.service.EmailService;
import co.edu.escuelaing.cvds.ClothCraft.service.SessionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserService userService;
    private WardrobeService wardrobeService;
    private CalendaryService calendaryService;
    private SessionService sessionService;
    private EmailService emailService;
    private UserController userController;

    private UserDTO userDTO;
    private User user;
    private Wardrobe wardrobe;
    private Calendary calendary;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        wardrobeService = mock(WardrobeService.class);
        calendaryService = mock(CalendaryService.class);
        sessionService = mock(SessionService.class);
        emailService = mock(EmailService.class);
        userController = new UserController(userService, wardrobeService, calendaryService, sessionService, emailService);

        // Datos ficticios
        userDTO = new UserDTO();
        userDTO.setId("fake-id-12345");
        userDTO.setName("John Doe");
        userDTO.setEmail("johndoe@example.com");
        userDTO.setPassword("dummyPassword");  // Usar un valor ficticio no relacionado con credenciales reales
        userDTO.setUsername("exampleUsername");

        user = new User();
        user.setId("fake-id-12345");
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");
        user.setPassword(UserDTO.hashPassword("dummyPassword"));  // Usar el mismo valor ficticio
        user.setUsername("exampleUsername");

        wardrobe = new Wardrobe(user);
        wardrobe.setId("fake-wardrobe-id-12345");
        calendary = new Calendary(user);
        calendary.setId("fake-calendary-id-12345");
    }

    @Test
    public void testCreateUser() throws MalformedURLException, IOException {

        when(userService.createUser(any(User.class))).thenReturn(user);
        when(wardrobeService.createWardrobe(wardrobe)).thenReturn(wardrobe);
        when(calendaryService.createCalendary(calendary)).thenReturn(calendary);
        when(userService.updateUser(anyString(), eq(user))).thenReturn(user);

        ResponseEntity<String> response = userController.createUser(userDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Capturar los argumentos para verificar
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userService, times(1)).createUser(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        // Verificar los valores del usuario capturado
        assertEquals("fake-id-12345", capturedUser.getId());
        assertEquals("John Doe", capturedUser.getName());
        assertEquals("johndoe@example.com", capturedUser.getEmail());
        assertEquals(UserDTO.hashPassword("dummyPassword"), capturedUser.getPassword());
        assertEquals("exampleUsername", capturedUser.getUsername());

        // Verificar llamadas en orden
        InOrder inOrder = inOrder(userService, wardrobeService, calendaryService);
        inOrder.verify(userService).createUser(any(User.class));
        inOrder.verify(wardrobeService).createWardrobe(any(Wardrobe.class));
        inOrder.verify(calendaryService).createCalendary(any(Calendary.class));
        inOrder.verify(userService).updateUser(eq("fake-id-12345"), any(User.class));

        // Verificar todas las llamadas
        verify(userService, times(1)).createUser(any(User.class));
        verify(userService, times(1)).updateUser(eq("fake-id-12345"), any(User.class));
        verify(wardrobeService, times(1)).createWardrobe(any(Wardrobe.class));
        verify(calendaryService, times(1)).createCalendary(any(Calendary.class));
    }

    @Test
    public void testGetUserById() {
        String userId = "12345";
        User user = new User();
        user.setId(userId);

        when(userService.getUserById(userId)).thenReturn(user);

        ResponseEntity<UserDTO> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.toDTO(), response.getBody());
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    public void testGetProfileOfAUser() {

        when(userService.createUser(any(User.class))).thenReturn(user);
        when(wardrobeService.createWardrobe(wardrobe)).thenReturn(wardrobe);
        when(calendaryService.createCalendary(calendary)).thenReturn(calendary);
        when(userService.updateUser(anyString(), eq(user))).thenReturn(user);

        ResponseEntity<String> response1 = userController.createUser(userDTO);

        assertEquals(HttpStatus.CREATED, response1.getStatusCode());

        Map<String, Object> expectedUserData = new HashMap<>();
        expectedUserData.put("name", "John Doe");
        expectedUserData.put("username", "exampleUsername");
        expectedUserData.put("profileImage", null);
        expectedUserData.put("numItems", 0);
        expectedUserData.put("numOutfits", 0);

        when(userService.getUserById(user.getId())).thenReturn(user);

        ResponseEntity<Map<String, Object>> response = userController.getProfileOfAUser(user.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedUserData, response.getBody());
        verify(userService, times(1)).getUserById(user.getId());
    }

    @Test
    public void testGetUserByUniqueKey(){
        when(userService.createUser(any(User.class))).thenReturn(user);
        when(wardrobeService.createWardrobe(wardrobe)).thenReturn(wardrobe);
        when(calendaryService.createCalendary(calendary)).thenReturn(calendary);
        when(userService.updateUser(anyString(), eq(user))).thenReturn(user);

        ResponseEntity<String> response1 = userController.createUser(userDTO);

        assertEquals(HttpStatus.CREATED, response1.getStatusCode());

        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
        when(userService.getUserByUserName(user.getUsername())).thenReturn(user);
        
        ResponseEntity<UserDTO> response = userController.getUserByUniqueKey(user.getEmail());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.toDTO(), response.getBody());
        verify(userService, times(1)).getUserByEmail(user.getEmail());
        
        ResponseEntity<UserDTO> response2 = userController.getUserByUniqueKey(user.getUsername());

        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(user.toDTO(), response2.getBody());
        verify(userService, times(1)).getUserByEmail(user.getEmail());
        verify(userService, times(1)).getUserByUserName(user.getUsername());

        ResponseEntity<UserDTO> response3 = userController.getUserByUniqueKey(user.getId());

        assertEquals(HttpStatus.NOT_FOUND, response3.getStatusCode());
        verify(userService, times(1)).getUserByEmail(user.getEmail());
        verify(userService, times(1)).getUserByUserName(user.getUsername());

        ResponseEntity<UserDTO> response4 = userController.getUserByUniqueKey("Not a valid key");

        assertEquals(HttpStatus.NOT_FOUND, response4.getStatusCode());
        verify(userService, times(1)).getUserByEmail(user.getEmail());
        verify(userService, times(1)).getUserByUserName(user.getUsername());
    }

    @Test
    public void testUpdateUser() {
        when(userService.createUser(any(User.class))).thenReturn(user);
        when(wardrobeService.createWardrobe(wardrobe)).thenReturn(wardrobe);
        when(calendaryService.createCalendary(calendary)).thenReturn(calendary);
        when(userService.updateUser(anyString(), eq(user))).thenReturn(user);

        ResponseEntity<String> response1 = userController.createUser(userDTO);

        assertEquals(HttpStatus.CREATED, response1.getStatusCode());

        User user1 = new User();
        user1.setId("12345");
        user1.setName("John Doe Modified");
        user1.setEmail("johndoeModified@example.com");
        user1.setPassword(UserDTO.hashPassword("password"));
        user1.setUsername("exampleUsernameModified");
        
        when(userService.updateUser(anyString(), any(User.class))).thenReturn(user1);
        System.out.println(user1.getPhotoProfile());
        System.out.println(user.getPhotoProfile());

        ResponseEntity<UserDTO> response = userController.updateUser(user.getId(), user1.toDTO());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user1.toDTO(), response.getBody());
    }
}