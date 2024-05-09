package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Session;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.repository.SessionRepository;
import co.edu.escuelaing.cvds.ClothCraft.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import java.util.Collections;


import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;

    public LoginController(
            UserRepository userRepository,
            SessionRepository sessionRepository
    ) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("")
    public String login() {
        return "Yei";
    }

    @PostMapping("")
    public ResponseEntity<?> loginSubmit(@RequestParam Map<String, String> parameters,
                                         HttpServletResponse response) {
        User user = userRepository.findByEmail(parameters.get("email")).orElse(null);
        if (user == null) {
            // Handle user not found
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        } else if (!user.getPassword().equals(hashPassword(parameters.get("password")))) {
            // Handle incorrect password
            return ResponseEntity.badRequest().body("Contraseña incorrecta");
        } else {
            Session session = new Session(UUID.randomUUID(), Instant.now(), user);
            sessionRepository.save(session);

            System.out.println(session.getToken().toString());

            return ResponseEntity.ok().body(Collections.singletonMap("token", session.getToken().toString()));
        }
    }

    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<?> logoutSubmit(@RequestParam Map<String, String> parameters, HttpServletResponse response) {
        String authTokenHeader = parameters.get("Cookie");
        System.out.println("Auth token from body: " + authTokenHeader);

        if (authTokenHeader != null) {
            UUID token = UUID.fromString(authTokenHeader);
            Session session = sessionRepository.findByToken(token);

            if (session != null) {
                sessionRepository.delete(session);
            }

            System.out.println("Cookie and session deleted");
            return ResponseEntity.ok("Logged out successfully");
        } else {
            return ResponseEntity.badRequest().body("No authToken found in the body");
        }
    }



    @GetMapping("register")
    public String register() {
        return "login/register";
    }

    @PostMapping("register")
    public String registerSubmit(@RequestParam Map<String, String> parameters) {
        User user = new User(
                parameters.get("email"),
                parameters.get("password")
        );
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("protected/example")
    public String protectedExample() {
        return "login/protected";
    }

    @GetMapping("protected/admin")
    public String protectedAdmin() {
        return "login/admin";
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 32) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}