package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Session;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.UserDTO;
import co.edu.escuelaing.cvds.ClothCraft.repository.SessionRepository;
import co.edu.escuelaing.cvds.ClothCraft.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.Collections;

import java.time.Instant;
import java.util.UUID;

/*
 * Class that handles the login and logout of the users
 */
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
    public ResponseEntity<?> loginSubmit(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        User user = userRepository.findByEmail(userDTO.getEmail()).orElse(null);
        if (user == null) {
            user = userRepository.findByUsername('@'+userDTO.getUsername()).orElse(null);
        }
        ResponseEntity<?> responseEntity;
        String hashedPassword = UserDTO.hashPassword(userDTO.getPassword());
        if (user == null) {
            responseEntity = ResponseEntity.badRequest().body("User not found");
        } else if (!hashedPassword.equals(user.getPassword())) {
            responseEntity = ResponseEntity.badRequest().body("Wrong password");
        } else if (!user.isVerified()){
            responseEntity = ResponseEntity.badRequest().body("User not verified");
        } else {
            // Create a new session
            Session session = new Session(UUID.randomUUID(), Instant.now(), user);
            sessionRepository.save(session);
            // Set to the response the cookie with the token
            response.addHeader("Set-Cookie", "authToken=" + session.getToken().toString() + "; Path=/; Secure; HttpOnly; SameSite=None");
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
            return ResponseEntity.ok("Logged out successfully");
        } else
            return ResponseEntity.badRequest().body("No authToken found in the body");
    }

}
