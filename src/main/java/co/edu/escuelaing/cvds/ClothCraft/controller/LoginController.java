package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Session;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.UserDTO;
import co.edu.escuelaing.cvds.ClothCraft.repository.SessionRepository;
import co.edu.escuelaing.cvds.ClothCraft.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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


@Slf4j
@Controller
@RequestMapping(value = "/login")
public class LoginController {

    private final UserRepository userRepository;

    private final SessionRepository sessionRepository;

    public LoginController(
            UserRepository userRepository,
            SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("")
    public String login() {
        return "Yei";
    }

    @PostMapping("")
    public ResponseEntity<?> loginSubmit(@RequestBody UserDTO userDTO,
            HttpServletResponse response) {
        User user = userRepository.findByEmail(userDTO.getEmail()).orElse(null);
        if (user == null) {
            // Handle user not found
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        } else if (!user.getPassword().equals(hashPassword(userDTO.getPassword()))) {
            // Handle incorrect password
            return ResponseEntity.badRequest().body("Contraseña incorrecta");
        } else {
            Session session = new Session(UUID.randomUUID(), Instant.now(), user);
            sessionRepository.save(session);

            System.out.println(session.getToken().toString());

            response.addHeader("Set-Cookie", "authToken=" + session.getToken().toString()
                    + "; Domain:mango-cliff-06b900910.5.azurestaticapps.net; Path=/; Secure; SameSite=None");
            System.out.println("Cookie set" + response.getHeader("Set-Cookie"));
            return ResponseEntity.ok().body(Collections.singletonMap("token", session.getToken().toString()));
        }
    }

    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<?> logoutSubmit(HttpServletRequest request, HttpServletResponse response) {
        String res = "Headers";
        for (java.util.Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements();){
            String header = headerNames.nextElement();       
            res += " new header:" + request.getHeader(header) + "\n";               
        }  
        log.info(res);

        String cookie = request.getHeader("cookie");
        String authTokenHeader = cookie.replace("authToken=", "");
        
        log.info("Auth token from body: " + authTokenHeader);
        if (authTokenHeader != null) {
            UUID token = UUID.fromString(authTokenHeader);
            Session session = sessionRepository.findByToken(token);

            if (session != null) {
                sessionRepository.delete(session);
            }

            response.addHeader("Set-Cookie", "authToken=; Path=/; Secure; SameSite=None");
            log.info("The opp is succesful");
            return ResponseEntity.ok("Logged out successfully");
        } else {
            log.error("The token is not assigned");
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
                parameters.get("password"));
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
