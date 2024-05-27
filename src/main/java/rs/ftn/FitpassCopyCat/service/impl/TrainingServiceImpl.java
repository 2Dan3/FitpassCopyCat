package rs.ftn.FitpassCopyCat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ftn.FitpassCopyCat.model.DTO.TrainingCreateDTO;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.Training;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.model.entity.WorkDay;
import rs.ftn.FitpassCopyCat.repository.TrainingRepository;
import rs.ftn.FitpassCopyCat.service.TrainingService;

import java.time.DayOfWeek;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {
    private TrainingRepository trainingRepository;
    @Autowired
    public TrainingServiceImpl(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }


    @Override
    public List<Training> getTrainingsBy(User trainee) {
        return trainingRepository.findAllByTrainee(trainee.getId());
    }

    @Override
    public Training scheduleTraining(TrainingCreateDTO trainingData, Facility trainingFacility, User trainee) {
        Integer overlappingTrainingsCount = trainingRepository.countOverlappingTrainings(trainingData.getFromHours(), trainingData.getUntilHours(), trainingData.getFacilityId());
        if (overlappingTrainingsCount > 0)
            return null;

        return trainingRepository.save(new Training(trainingData, trainingFacility, trainee));
    }

    @Override
    public boolean reservationIsWithinWorkHours(TrainingCreateDTO trainingData, Facility trainingFacility) {
        for (WorkDay wd : trainingFacility.getWorkDays()) {
            if (wd.getDay().equals(trainingData.getFromHours().getDayOfWeek())) {
                if (wd.getFromHours().isAfter(trainingData.getFromHours().toLocalTime()) || wd.getUntilHours().isBefore(trainingData.getUntilHours().toLocalTime()))
                    return false;
                break;
            }
        }
        return true;
    }
}
