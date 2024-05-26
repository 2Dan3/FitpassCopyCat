package rs.ftn.FitpassCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rs.ftn.FitpassCopyCat.model.DTO.FacilityCreateDTO;
import rs.ftn.FitpassCopyCat.model.DTO.FacilityResponseDTO;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.service.FacilityService;
import rs.ftn.FitpassCopyCat.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "${apiPrefix}/facilities")
public class FacilityController {
    private FacilityService facilityService;
    private UserService userService;
    @Autowired
    public FacilityController(FacilityService facilityService, UserService userService) {
        this.facilityService = facilityService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<FacilityResponseDTO> createFacility(@Valid @RequestBody FacilityCreateDTO facilityData, Authentication loggedUser) {
//        eliminate possibility of an existing Facility under the same "Name"

        if (loggedUser == null)
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);

        User newManager = userService.findByEmail(loggedUser.getName());
        Facility newFacility = new Facility(facilityData, newManager);
        userService.save(newManager);
        newFacility = facilityService.save(newFacility);

        return new ResponseEntity<>(new FacilityResponseDTO(newFacility), HttpStatus.OK);
    }

    @PutMapping(path = "/{facilityId}/managers")
    public ResponseEntity<Void> putManager(@PathVariable(name = "facilityId") Long facilityId, @RequestBody String userEmail) {

        Facility targetedFacility = facilityService.findById(facilityId);
        if (targetedFacility == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        User wantedUser = userService.findByEmail(userEmail);
        if (wantedUser == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        wantedUser.getManagedFacilities().add(targetedFacility);
        targetedFacility.getManagers().add(wantedUser);

        if (!targetedFacility.getActive())
            targetedFacility.setActive(true);

        userService.save(wantedUser);
        facilityService.save(targetedFacility);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(path = "/{facilityId}/managers")
    public ResponseEntity<Void> removeManager(@PathVariable(name = "facilityId") Long facilityId, @RequestBody String managerEmail) {

        Facility targetedFacility = facilityService.findById(facilityId);
        if (targetedFacility == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        User wantedManager = userService.findByEmail(managerEmail);
        if (wantedManager == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        if (wantedManager.getManagedFacilities().remove(targetedFacility) == false && targetedFacility.getManagers().remove(wantedManager) == false)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        if (targetedFacility.getManagers().isEmpty())
            targetedFacility.setActive(false);

        userService.save(wantedManager);
        facilityService.save(targetedFacility);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
