package rs.ftn.FitpassCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rs.ftn.FitpassCopyCat.model.DTO.FacilityCreateDTO;
import rs.ftn.FitpassCopyCat.model.DTO.FacilityOverviewDTO;
import rs.ftn.FitpassCopyCat.model.DTO.FacilityResponseDTO;
import rs.ftn.FitpassCopyCat.model.DTO.ReviewOverviewDTO;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.Review;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.service.FacilityService;
import rs.ftn.FitpassCopyCat.service.ReviewService;
import rs.ftn.FitpassCopyCat.service.UserService;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static rs.ftn.FitpassCopyCat.repository.FacilityFilterRepositoryImpl.DAY_OF_WEEK_DEFAULT_VALUE;

@RestController
@RequestMapping(path = "${apiPrefix}/facilities")
public class FacilityController {
    private FacilityService facilityService;
    private UserService userService;
    private ReviewService reviewService;
    @Autowired
    public FacilityController(FacilityService facilityService, UserService userService,
                              ReviewService reviewService) {
        this.facilityService = facilityService;
        this.userService = userService;
        this.reviewService = reviewService;
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

        return new ResponseEntity<>(new FacilityResponseDTO(newFacility, true), HttpStatus.OK);
    }

    @GetMapping(path = "/{facilityId}")
    public ResponseEntity<FacilityResponseDTO> getFacility(@PathVariable(name = "facilityId") Long facilityId, Authentication authentication) {

        Facility facility = facilityService.findById(facilityId);
        if (facility == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User loggedUser = userService.findByEmail(authentication.getName());
        final boolean loggedUserManagesFacility = userService.managesFacility(facility.getId(), loggedUser);

        return new ResponseEntity<>(new FacilityResponseDTO(facility, loggedUserManagesFacility), HttpStatus.OK);
    }

//    @GetMapping(path = "/active")
//    public ResponseEntity<List<FacilityOverviewDTO>> getActiveFacilities() {
//
//        List<Facility> facilities = facilityService.findActive();
//
//        List<FacilityOverviewDTO> facilityOverviewDTOs = new ArrayList<>();
//        for (Facility f : facilities) {
//            facilityOverviewDTOs.add(new FacilityOverviewDTO(f));
//        }
//
//        return new ResponseEntity<>(facilityOverviewDTOs, HttpStatus.OK);
//    }
//
//    @GetMapping(path = "/all")
//    public ResponseEntity<List<FacilityOverviewDTO>> getActiveAndInactiveFacilities() {
//
//        List<Facility> facilities = facilityService.findAll();
//
//        List<FacilityOverviewDTO> facilityOverviewDTOs = new ArrayList<>();
//        for (Facility f : facilities) {
//            facilityOverviewDTOs.add(new FacilityOverviewDTO(f));
//        }
//
//        return new ResponseEntity<>(facilityOverviewDTOs, HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<List<FacilityOverviewDTO>> getFacilitiesByFilters(@RequestParam(name = "cities", required = false) List<String> cities, @RequestParam(name = "disciplines", required = false) List<String> disciplineNames,
                                                                            @RequestParam(name = "ratingMin", required = false) Integer ratingMin, @RequestParam(name = "ratingMax", required = false) Integer ratingMax,
                                                                            @RequestParam(name = "weekDay", required = false, defaultValue = DAY_OF_WEEK_DEFAULT_VALUE) String dayOfWeek,
                                                                            @RequestParam(name = "hoursFrom", required = false) @DateTimeFormat(pattern = "HH:mm") final LocalTime hoursFrom,
                                                                            @RequestParam(name = "hoursUntil", required = false) @DateTimeFormat(pattern = "HH:mm") final LocalTime hoursUntil,
                                                                            @RequestParam(name = "active", required = false, defaultValue = "true") Boolean active) {
        try {
            List<Facility> facilities = facilityService.findByFilters(cities, disciplineNames, ratingMin, ratingMax, dayOfWeek, hoursFrom, hoursUntil, active);

            List<FacilityOverviewDTO> facilityOverviewDTOs = new ArrayList<>();
            for (Facility f : facilities) {
                facilityOverviewDTOs.add(new FacilityOverviewDTO(f));
            }

            return new ResponseEntity<>(facilityOverviewDTOs, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

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

    @GetMapping(path = "/{facilityId}/reviews")
    public ResponseEntity<List<ReviewOverviewDTO>> getAllReviewsOnFacility(@PathVariable(name = "facilityId") Long facilityId) {
        Facility facility = facilityService.findById(facilityId);
        if (facility == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<ReviewOverviewDTO> reviewDTOs = new ArrayList<>();
        List<Review> reviewsWritten = reviewService.getReviewsOnFacility(facility);
        for (Review r : reviewsWritten) {
            reviewDTOs.add(new ReviewOverviewDTO(r));
        }

        return new ResponseEntity<>(reviewDTOs, HttpStatus.OK);
    }
}
