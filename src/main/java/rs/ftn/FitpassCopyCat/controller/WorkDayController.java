package rs.ftn.FitpassCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rs.ftn.FitpassCopyCat.model.DTO.DisciplineCreateDTO;
import rs.ftn.FitpassCopyCat.model.DTO.WorkDayDTO;
import rs.ftn.FitpassCopyCat.model.entity.Discipline;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.model.entity.WorkDay;
import rs.ftn.FitpassCopyCat.service.FacilityService;
import rs.ftn.FitpassCopyCat.service.UserService;
import rs.ftn.FitpassCopyCat.service.WorkDayService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "${apiPrefix}/hours")
public class WorkDayController {
    private WorkDayService workDayService;
    private FacilityService facilityService;
    private UserService userService;
    @Autowired
    public WorkDayController(WorkDayService workDayService, FacilityService facilityService,
                                UserService userService) {
        this.workDayService = workDayService;
        this.facilityService = facilityService;
        this.userService = userService;
    }

    @PostMapping(path = "/{facilityId}")
    public ResponseEntity<Void> setWorkDayToFacility(@PathVariable(name = "facilityId") Long facilityId, @Valid @RequestBody WorkDayDTO data, Authentication authentication) {

        Facility targetedFacility = facilityService.findById(facilityId);
        if (targetedFacility == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        User manager = userService.findByEmail(authentication.getName());
        if ( ! userService.managesFacility(targetedFacility.getId(), manager) )
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        WorkDay existingWorkDay = workDayService.findWorkDay(data, targetedFacility);
        if (existingWorkDay != null) {
            existingWorkDay.setFacility(null);
            targetedFacility.getWorkDays().remove(existingWorkDay);
        }
        WorkDay newWorkDay = new WorkDay(data, targetedFacility);
        targetedFacility.getWorkDays().add(newWorkDay);

        facilityService.save(targetedFacility);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(path = "/{facilityId}")
    public ResponseEntity<Void> removeWorkDayFromFacility(@PathVariable(name = "facilityId") Long facilityId, @RequestBody Long workDayID, Authentication authentication) {

        Facility targetedFacility = facilityService.findById(facilityId);
        if (targetedFacility == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        User manager = userService.findByEmail(authentication.getName());
        if ( ! userService.managesFacility(targetedFacility.getId(), manager) )
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        WorkDay existingWorkDay = workDayService.findById(workDayID);
        if (existingWorkDay == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        existingWorkDay.setFacility(null);
        targetedFacility.getWorkDays().remove(existingWorkDay);
        facilityService.save(targetedFacility);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
