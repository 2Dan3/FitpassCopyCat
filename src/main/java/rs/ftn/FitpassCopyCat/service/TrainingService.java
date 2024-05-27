package rs.ftn.FitpassCopyCat.service;

import rs.ftn.FitpassCopyCat.model.DTO.TrainingCreateDTO;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.Training;
import rs.ftn.FitpassCopyCat.model.entity.User;

import java.util.List;

public interface TrainingService {
    List<Training> getTrainingsBy(User trainee);

    Training scheduleTraining(TrainingCreateDTO trainingData, Facility trainingFacility, User trainee);

    boolean reservationIsWithinWorkHours(TrainingCreateDTO trainingData, Facility trainingFacility);

    int countTrainings(User trainee, Facility facility);
}
