package co.edu.escuelaing.cvds.ClothCraft.repository;

import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OutfitRepository extends JpaRepository<Outfit, String> {

    
    //@Query("SELECT c.name FROM outfit_clothing oc JOIN  clothing c WHERE oc.outfit_id  = :outfitId and c.id = oc.clothing_id")
    @Query("SELECT c.name FROM Outfit o JOIN o.clothes c WHERE o.id = :outfitId")
    String[] getClothingNameByOutfitId(@Param("outfitId") String id);
}