package rs.ftn.FitpassCopyCat.service;

import rs.ftn.FitpassCopyCat.model.entity.Facility;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

public interface FacilityService {
    Facility findById(Long id);

    Facility save(Facility newFacility);

    void updateFacilityRating(Facility facilityReviewed);

    List<Facility> findActive();

    Facility findByCommentLeft(Long commentId);

    List<Facility> findAll();

    List<Facility> findActiveByFilters(List<String> cities, List<String> disciplineNames, Integer ratingMin, Integer ratingMax, DayOfWeek dayOfWeek, LocalTime hoursFrom, LocalTime hoursUntil);
}
