package rs.ftn.FitpassCopyCat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.repository.FacilityFilterRepositoryImpl;
import rs.ftn.FitpassCopyCat.repository.FacilityRepository;
import rs.ftn.FitpassCopyCat.repository.ReviewRepository;
import rs.ftn.FitpassCopyCat.service.FacilityService;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class FacilityServiceImpl implements FacilityService {
    private FacilityRepository facilityRepository;
    private FacilityFilterRepositoryImpl facilityFilterRepository;
    private ReviewRepository reviewRepository;
    @Autowired
    public FacilityServiceImpl(FacilityRepository facilityRepository, ReviewRepository reviewRepository){
        this.facilityRepository = facilityRepository;
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Facility findById(Long id) {
        Optional<Facility> foundFacility = facilityRepository.findById(id);
        return foundFacility.orElse(null);
    }

    @Override
    public Facility save(Facility newFacility) {
        return facilityRepository.save(newFacility);
    }

    @Override
    public void updateFacilityRating(Facility facilityReviewed) {
        Double newlyCalculatedRating = reviewRepository.findAverageReviewRating(facilityReviewed.getId());
        facilityReviewed.setTotalRating(newlyCalculatedRating);
        facilityRepository.save(facilityReviewed);
    }

    @Override
    public List<Facility> findActive() {
        return facilityRepository.findByActiveTrue();
    }

    @Override
    public Facility findByCommentLeft(Long commentId) {
        return facilityRepository.findByCommentLeft(commentId);
    }

    @Override
    public List<Facility> findAll() {
        return facilityRepository.findAll();
    }

    @Override
    public List<Facility> findByFilters(List<String> cities,
                                        List<String> disciplineNames,
                                        Integer ratingMin,
                                        Integer ratingMax,
                                        String dayOfWeek,
                                        LocalTime hoursFrom,
                                        LocalTime hoursUntil,
                                        Boolean active) throws IllegalArgumentException{
        return facilityFilterRepository.findByFilters(cities, disciplineNames, ratingMin, ratingMax, dayOfWeek, hoursFrom, hoursUntil, active);
    }

    @Override
    public List<Facility> findVisitedByUser(User user) {
        return facilityRepository.findVisitedByUser(user.getId());
    }

    @Override
    public List<Facility> findMostPopular() {
        return facilityRepository.findMostPopular();
    }

    @Override
    public List<Facility> findUnexplored(User user, Boolean limitTo5) {
        return limitTo5 ? facilityRepository.findUnexploredLimit5(user.getId()) : facilityRepository.findUnexplored(user.getId());
    }

}
