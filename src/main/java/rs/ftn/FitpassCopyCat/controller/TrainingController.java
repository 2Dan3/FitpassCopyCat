package rs.ftn.FitpassCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rs.ftn.FitpassCopyCat.model.DTO.TrainingCreateDTO;
import rs.ftn.FitpassCopyCat.model.DTO.TrainingDTO;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.Training;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.service.FacilityService;
import rs.ftn.FitpassCopyCat.service.TrainingService;
import rs.ftn.FitpassCopyCat.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/trainings")
public class TrainingController {
    private TrainingService trainingService;
    private UserService userService;
    private FacilityService facilityService;
    @Autowired
    public TrainingController(TrainingService trainingService, UserService userService, FacilityService facilityService) {
        this.trainingService = trainingService;
        this.userService = userService;
        this.facilityService = facilityService;
    }


    @GetMapping(path = "/my")
    public ResponseEntity<List<TrainingDTO>> getAllTrainingsOfLoggedUser(Authentication authenticatedUser) {
        User loggedTrainee = userService.findByEmail(authenticatedUser.getName());
        if (loggedTrainee == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        List<Training> completedTrainings = trainingService.getTrainingsBy(loggedTrainee);
        List<TrainingDTO> trainingDTOs = new ArrayList<>();

        for (Training t : completedTrainings) {
            trainingDTOs.add(new TrainingDTO(t));
        }
        return new ResponseEntity<>(trainingDTOs, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TrainingDTO> scheduleTrainingForLoggedUser(@Valid @RequestBody TrainingCreateDTO trainingData, Authentication authenticatedUser) {
        User loggedTrainee = userService.findByEmail(authenticatedUser.getName());
        if (loggedTrainee == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        Facility trainingFacility = facilityService.findById(trainingData.getFacilityId());
        if (trainingFacility == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

        Training scheduledTraining = trainingService.scheduleTraining(trainingData, trainingFacility, loggedTrainee);
        if (scheduledTraining == null)
            return new ResponseEntity<>(null, HttpStatus.NOT_ACCEPTABLE);

        return new ResponseEntity<>(new TrainingDTO(scheduledTraining), HttpStatus.OK);
    }
}
