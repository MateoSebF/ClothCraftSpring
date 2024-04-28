package co.edu.escuelaing.cvds.ClothCraft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;
@Repository
public interface WardrobeRepository extends JpaRepository<Wardrobe, String>{}