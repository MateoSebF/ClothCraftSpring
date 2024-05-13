package co.edu.escuelaing.cvds.ClothCraft.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.ClothingType;
@Repository
public interface ClothingRepository extends JpaRepository<Clothing, String>{

    List<Clothing> findByType(ClothingType type);
}
