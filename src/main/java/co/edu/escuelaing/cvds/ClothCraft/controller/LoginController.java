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
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.Collections;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.UUID;

/*
 * Class that handles the login and logout of the users
 */
@Slf4j
@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    /*
     * Method that handles the login of the user
     * 
     * @param userDTO, the user to be logged in
     * 
     * @param response, the response to be sent to the server
     * 
     * @return ResponseEntity, the response to be sent to the server
     */
    @PostMapping("")
    public ResponseEntity<?> loginSubmit(@RequestBody UserDTO userDTO,
            HttpServletResponse response) {
        User user = userRepository.findByEmail(userDTO.getEmail()).orElse(null);
        ResponseEntity<?> responseEntity;
        if (user == null)
            responseEntity = ResponseEntity.badRequest().body("User not found");
        else if (!user.getPassword().equals(hashPassword(userDTO.getPassword())))
            responseEntity = ResponseEntity.badRequest().body("Wrong password");
        else {
            // Create a new session
            Session session = new Session(UUID.randomUUID(), Instant.now(), user);
            sessionRepository.save(session);
            // Set to the response the cookie with the token
            response.addHeader("Set-Cookie", "authToken=" + session.getToken().toString()
                    + "; Path=/; Secure; SameSite=None");
            log.info("The user " + user.getEmail() + " has logged in successfully");
            responseEntity = ResponseEntity.ok().body(Collections.singletonMap("token", session.getToken().toString()));
        }
        return responseEntity;
    }

    /*
     * Method that handles the logout of the user
     * 
     * @param request, the request to be sent to the server
     * 
     * @param response, the response to be sent to the server
     * 
     * @return ResponseEntity, the response to be sent to the server
     */
    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<?> logoutSubmit(HttpServletRequest request,
            @CookieValue("authToken") String authToken, HttpServletResponse response) {
        if (authToken != null) {
            UUID token = UUID.fromString(authToken);
            Session session = sessionRepository.findByToken(token);
            if (session != null) {
                sessionRepository.delete(session);
            }
            // Delete the cookie and pass an empty value to the authToken
            response.addHeader("Set-Cookie", "authToken=; Path=/; Secure; SameSite=None");
            log.info("The user " + session.getUser().getEmail() + " has logged out successfully");
            return ResponseEntity.ok("Logged out successfully");
        } else
            return ResponseEntity.badRequest().body("No authToken found in the body");
    }

    /*
     * Method that hashes the password of the user
     * 
     * @param password, the password to be hashed
     * 
     * @return String, the hashed password
     */
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
