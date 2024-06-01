package rs.ftn.FitpassCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rs.ftn.FitpassCopyCat.model.DTO.DisciplineCreateDTO;
import rs.ftn.FitpassCopyCat.model.entity.Discipline;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.service.DisciplineService;
import rs.ftn.FitpassCopyCat.service.FacilityService;
import rs.ftn.FitpassCopyCat.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "${apiPrefix}/disciplinesAt")
public class DisciplineController {
    private DisciplineService disciplineService;
    private FacilityService facilityService;
    private UserService userService;
    @Autowired
    public DisciplineController(DisciplineService disciplineService, FacilityService facilityService,
                                UserService userService) {
        this.disciplineService = disciplineService;
        this.facilityService = facilityService;
        this.userService = userService;
    }

    @PostMapping(path = "/{facilityId}")
    public ResponseEntity<Void> addDisciplineToFacility(@PathVariable(name = "facilityId") Long facilityId, @Valid @RequestBody DisciplineCreateDTO data, Authentication authentication) {

        Facility targetedFacility = facilityService.findById(facilityId);
        if (targetedFacility == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        User manager = userService.findByEmail(authentication.getName());
//        if (wantedManager == null)
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if ( ! userService.managesFacility(targetedFacility.getId(), manager) )
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//
//        for (DisciplineCreateDTO data : disciplinesData) {
            Discipline newDiscipline = new Discipline(data, targetedFacility);
            targetedFacility.getDisciplines().add(newDiscipline);
//        }

        facilityService.save(targetedFacility);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(path = "/{disciplineId}")
    public ResponseEntity<Void> removeDisciplineFromFacility(@PathVariable(name = "disciplineId") Long disciplineId, Authentication authentication) {
        Discipline discipline = disciplineService.findById(disciplineId);
        if (discipline == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        Facility targetedFacility = discipline.getFacility();

        User manager = userService.findByEmail(authentication.getName());
//        if (wantedManager == null)
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if ( ! userService.managesFacility(targetedFacility.getId(), manager) )
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();


//        for (DisciplineCreateDTO data : disciplinesData) {
            targetedFacility.getDisciplines().remove(discipline);
            disciplineService.remove(discipline);
//        }

        facilityService.save(targetedFacility);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
