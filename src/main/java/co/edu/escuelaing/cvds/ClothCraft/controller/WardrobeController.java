package co.edu.escuelaing.cvds.ClothCraft.controller;

import co.edu.escuelaing.cvds.ClothCraft.model.Clothing;
import co.edu.escuelaing.cvds.ClothCraft.model.ClothingType;
import co.edu.escuelaing.cvds.ClothCraft.model.Outfit;
import co.edu.escuelaing.cvds.ClothCraft.model.User;
import co.edu.escuelaing.cvds.ClothCraft.model.Wardrobe;
import co.edu.escuelaing.cvds.ClothCraft.model.DTO.WardrobeDTO;
import co.edu.escuelaing.cvds.ClothCraft.service.ClothingService;
import co.edu.escuelaing.cvds.ClothCraft.service.OutfitService;
import co.edu.escuelaing.cvds.ClothCraft.service.UserService;
import co.edu.escuelaing.cvds.ClothCraft.service.WardrobeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * The class WardrobeController is a controller that allows to manage the wardrobes
 */
@RestController
@RequestMapping("/wardrobe")
public class WardrobeController {

    @Autowired
    private WardrobeService wardrobeService;
    @Autowired
    private UserService userService;
    @Autowired
    private ClothingService clothingService;
    @Autowired
    private OutfitService outfitService;

    /*
     * Method that gets a wardrobe by its id
     * 
     * @param id, the id of the wardrobe
     * 
     * @return ResponseEntity<WardrobeDTO>, the wardrobe with the id
     */
    @GetMapping("/{id}")
    public ResponseEntity<WardrobeDTO> getWardrobeById(@PathVariable String id) {
        Wardrobe wardrobe = wardrobeService.getWardrobeById(id);
        if (wardrobe != null)
            return new ResponseEntity<>(wardrobe.toDTO(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that gets all the wardrobes
     * 
     * @return ResponseEntity<List<WardrobeDTO>>, the list of all the wardrobes
     */
    @GetMapping("/all")
    public ResponseEntity<List<WardrobeDTO>> getAllWardrobes() {
        List<Wardrobe> wardrobeList = wardrobeService.getAllWardrobes();
        List<WardrobeDTO> wardrobeDTOList = wardrobeList.stream()
                .map(Wardrobe::toDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(wardrobeDTOList, HttpStatus.OK);
    }

    /*
     * Method that gets all the layers types
     * 
     * @param userId, the id of the user
     * 
     * @return ResponseEntity<List<String>>, the list of all the layers types
     */
    @GetMapping("/layersTypes")
    public ResponseEntity<List<String>> getLayers(
            @RequestParam(name = "userId", required = true) String userId) {
        User user = userService.getUserById(userId);
        Wardrobe wardrobe = user.getWardrobe();
        if (wardrobe != null)
            return new ResponseEntity<>(
                    wardrobe.getLayers().stream().map(ClothingType::name).collect(Collectors.toList()), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that gets all the clothes of a wardrobe
     * 
     * @param userId, the id of the user
     * 
     * @return ResponseEntity<List<ClothingDTO>>, the list of all the clothes of a
     * wardrobe
     */
    @PostMapping
    public ResponseEntity<WardrobeDTO> createWardrobe(@RequestBody WardrobeDTO wardrobeDTO) {
        Wardrobe wardrobe = wardrobeService.createWardrobe(convertToObject(wardrobeDTO));
        if (wardrobe != null)
            return new ResponseEntity<>(wardrobe.toDTO(), HttpStatus.CREATED);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    /*
     * Method that updates a wardrobe
     * 
     * @param id, the id of the wardrobe to be updated
     * 
     * @param wardrobeDTO, the wardrobe to be updated
     * 
     * @return ResponseEntity<WardrobeDTO>, the updated wardrobe
     */
    @PutMapping("/{id}")
    public ResponseEntity<WardrobeDTO> updateWardrobe(@PathVariable String id, @RequestBody WardrobeDTO wardrobeDTO) {
        Wardrobe updatedWardrobe = wardrobeService.updateWardrobe(id, convertToObject(wardrobeDTO));
        if (updatedWardrobe != null)
            return new ResponseEntity<>(updatedWardrobe.toDTO(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that deletes a wardrobe
     * 
     * @param id, the id of the wardrobe to be deleted
     * 
     * @return ResponseEntity<Void>, the status of the deletion
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWardrobe(@PathVariable String id) {
        boolean deleted = wardrobeService.deleteWardrobe(id);
        if (deleted)
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /*
     * Method that gets a wardrobe by its id
     * 
     * @param id, the id of the wardrobe
     * 
     * @return Wardrobe, the wardrobe with the id
     */
    public Wardrobe getWardrobeEntityById(String id) {
        Wardrobe wardrobe = wardrobeService.getWardrobeById(id);
        return wardrobe;
    }

    /*
     * Method that converts a WardrobeDTO to a Wardrobe
     * 
     * @param wardrobeDTO, the wardrobe to be converted
     * 
     * @return Wardrobe, the wardrobe converted
     */
    private Wardrobe convertToObject(WardrobeDTO wardrobeDTO) {
        User user = wardrobeDTO.getUserId() != null ? userService.getUserById(wardrobeDTO.getUserId()) : null;
        Set<Clothing> clothings = new HashSet<>();
        for (String clothingId : wardrobeDTO.getClothesIds())
            clothings.add(clothingService.getClothingById(clothingId));
        Set<Outfit> outfits = new HashSet<>();
        for (String outfitId : wardrobeDTO.getOutfitsIds())
            outfits.add(outfitService.getOutfitById(outfitId));
        return wardrobeDTO.toEntity(wardrobeDTO.getId(), user, clothings, outfits);
    }
}
