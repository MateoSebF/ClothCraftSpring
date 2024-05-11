package co.edu.escuelaing.cvds.ClothCraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import co.edu.escuelaing.cvds.ClothCraft.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String>{
    public Optional<User> findByEmail(String email);
    public Optional<User> findByUsername(String email);
}