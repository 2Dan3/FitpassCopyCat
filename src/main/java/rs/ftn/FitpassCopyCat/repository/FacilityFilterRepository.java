package rs.ftn.FitpassCopyCat.repository;

import rs.ftn.FitpassCopyCat.model.entity.Facility;

import java.time.LocalTime;
import java.util.List;

interface FacilityFilterRepository {

    List<Facility> findByFilters(List<String> cities, List<String> disciplineNames,
                                 Integer ratingMin, Integer ratingMax,
                                 String dayOfWeek,
                                 LocalTime hoursFrom, LocalTime hoursUntil,
                                 Boolean active) throws IllegalArgumentException;
}
