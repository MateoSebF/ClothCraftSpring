package co.edu.escuelaing.cvds.ClothCraft.service;

import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            handleDataIntegrityViolationException(ex);
            return null; // This return will never execute because exceptions are thrown within handleDataIntegrityViolationException
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    private void handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        if (message != null) {
            if (message.contains("user.UK_ob8kqyqqgmefl0aco34akdtpe")) {
                throw new RuntimeException("The 'email' field already exists. Please choose another email.", ex);
            } else if (message.contains("user.UK_sb8bbouer5wak8vyiiy4pf2bx")) {
                throw new RuntimeException("The 'username' field already exists. Please choose another username.", ex);
            }
        }
        throw ex; // Re-throw the original exception if no specific message is identified
    }
    
    

    public User updateUser(String id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            user.setId(existingUser.getId());
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    public boolean deleteUser(String id) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            userRepository.delete(existingUser);
            return true;
        } else {
            return false;
        }
    }

    public User getUserByEmail(String email) {return userRepository.findByEmail(email).orElse(null);}

    public User getUserByUserName(String username) {return userRepository.findByUsername(username).orElse(null);}
}
