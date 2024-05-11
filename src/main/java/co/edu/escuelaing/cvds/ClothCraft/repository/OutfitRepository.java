package co.edu.escuelaing.cvds.ClothCraft.repository;

import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutfitRepository extends JpaRepository<Outfit, String> {
}