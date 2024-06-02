package rs.ftn.FitpassCopyCat.service;

import rs.ftn.FitpassCopyCat.model.entity.Facility;

import java.time.LocalTime;
import java.util.List;

public interface FacilityService {
    Facility findById(Long id);

    Facility save(Facility newFacility);

    void updateFacilityRating(Facility facilityReviewed);

    List<Facility> findActive();

    Facility findByCommentLeft(Long commentId);

    List<Facility> findAll();

    List<Facility> findByFilters(List<String> cities, List<String> disciplineNames, Integer ratingMin, Integer ratingMax, String dayOfWeek, LocalTime hoursFrom, LocalTime hoursUntil, Boolean active) throws IllegalArgumentException;
}
