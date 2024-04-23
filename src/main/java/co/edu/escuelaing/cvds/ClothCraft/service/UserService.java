package co.edu.escuelaing.cvds.ClothCraft.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.repository.UserRepository;

/**
 * UserService
 */
@Service
public class UserService {
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User addUser(User user){
        User validator = null;
        if (userRepository.findById(user.getId()).isEmpty()){
            validator = userRepository.save(user);
        }
        return validator;
    }

    public User updateUser(User user){
        User validator = null;
        if (userRepository.findById(user.getId()).isPresent()){
            validator = userRepository.save(user);
        }
        return validator;
    }

    public boolean deleteUserById(String id){
        boolean validator = false;
        if (userRepository.findById(id).isPresent()){
            userRepository.deleteById(id);
            validator = true;
        }
        return validator;
    }

    public boolean deleteUser(User user){
        boolean validator = false;
        if (userRepository.findById(user.getId()).isPresent()){
            userRepository.delete(user);
            validator = true;
        }
        return validator;
    }

    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}