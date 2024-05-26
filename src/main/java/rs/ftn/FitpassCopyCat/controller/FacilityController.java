package rs.ftn.FitpassCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
