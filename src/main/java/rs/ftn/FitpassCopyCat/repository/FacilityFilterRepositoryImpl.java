package rs.ftn.FitpassCopyCat.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import rs.ftn.FitpassCopyCat.model.entity.Discipline;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.WorkDay;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public class FacilityFilterRepositoryImpl implements FacilityFilterRepository {
    public static final String DAY_OF_WEEK_DEFAULT_VALUE = "";
    @Autowired
    private EntityManager em;

    @Override
    public List<Facility> findByFilters(List<String> cities,
                                        List<String> disciplineNames,
                                        Integer ratingMin,
                                        Integer ratingMax,
                                        String dayOfWeek,
                                        LocalTime hoursFrom,
                                        LocalTime hoursUntil,
                                        Boolean active) throws IllegalArgumentException{

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Facility> criteriaQuery = criteriaBuilder.createQuery(Facility.class);
        Root<Facility> facility = criteriaQuery.from(Facility.class);
        Predicate finalPredicate = null;
        Predicate predicateRatingMin;
        Predicate predicateRatingMax;
        Predicate predicateActive;

        Predicate predicateWorkDay = null;
        Join<WorkDay, Facility> facilityWorkDays = facility.join("workDays");
        if ( !DAY_OF_WEEK_DEFAULT_VALUE.equals(dayOfWeek) ){
            try {
                DayOfWeek weekDay = DayOfWeek.valueOf(dayOfWeek);
                Predicate predicateDayOfWeek = criteriaBuilder.equal(facilityWorkDays.get("day"), weekDay);
                addPredicateToWorkDay(predicateDayOfWeek, predicateWorkDay, criteriaBuilder);
            }catch (IllegalArgumentException e) {
                throw new IllegalArgumentException();
            }
        }
        if (hoursFrom != null) {
            Predicate predicateWorkHoursFrom = criteriaBuilder.lessThanOrEqualTo(facilityWorkDays.get("fromHours"), hoursFrom);
            addPredicateToWorkDay(predicateWorkHoursFrom, predicateWorkDay, criteriaBuilder);
        }
        if (hoursUntil != null) {
            Predicate predicateWorkHoursUntil = criteriaBuilder.greaterThanOrEqualTo(facilityWorkDays.get("untilHours"), hoursUntil);
            addPredicateToWorkDay(predicateWorkHoursUntil, predicateWorkDay, criteriaBuilder);
        }
        if (predicateWorkDay != null) {
            appendToFinalPredicate(predicateWorkDay, finalPredicate, criteriaBuilder);
        }

        Predicate predicateCities = null;
        for (String city : cities) {
            Predicate predicateCity = criteriaBuilder.equal(facility.get("city"), city);
            addPredicateToCities(predicateCity, predicateCities, criteriaBuilder);
        }
        if (predicateCities != null) {
            appendToFinalPredicate(predicateCities, finalPredicate, criteriaBuilder);
        }

        Predicate predicateDisciplines = null;
        Join<Discipline, Facility> facilityDisciplines = facility.join("disciplines");
        for (String discipline : disciplineNames) {
            Predicate predicateDiscipline = criteriaBuilder.equal(facilityDisciplines.get("name"), discipline);
            addPredicateToDisciplines(predicateDiscipline, predicateCities, criteriaBuilder);
        }
        if (predicateDisciplines != null) {
            appendToFinalPredicate(predicateDisciplines, finalPredicate, criteriaBuilder);
        }

        if (ratingMin != null && ratingMin != 0) {
            predicateRatingMin = criteriaBuilder.greaterThanOrEqualTo(facility.get("totalRating"), ratingMin);
            appendToFinalPredicate(predicateRatingMin, finalPredicate, criteriaBuilder);
        }
        if (ratingMax != null && ratingMax != 0) {
            predicateRatingMax = criteriaBuilder.lessThanOrEqualTo(facility.get("totalRating"), ratingMax);
            appendToFinalPredicate(predicateRatingMax, finalPredicate, criteriaBuilder);
        }

        predicateActive = criteriaBuilder.equal(facility.get("active"), active);
        appendToFinalPredicate(predicateActive, finalPredicate, criteriaBuilder);

        criteriaQuery.where(finalPredicate);

        List<Facility> facilities = em.createQuery(criteriaQuery).getResultList();
        return facilities;
    }

    private void addPredicateToCities(Predicate predicateToAdd, Predicate predicateCities, CriteriaBuilder criteriaBuilder) {
        if (predicateCities == null) {
            predicateCities = predicateToAdd;
        }else {
            predicateCities = criteriaBuilder.or(predicateCities, predicateToAdd);
        }
    }

    private void addPredicateToDisciplines(Predicate predicateToAdd, Predicate predicateDisciplines, CriteriaBuilder criteriaBuilder) {
        if (predicateDisciplines == null) {
            predicateDisciplines = predicateToAdd;
        }else {
            predicateDisciplines = criteriaBuilder.or(predicateDisciplines, predicateToAdd);
        }
    }

    private void addPredicateToWorkDay(Predicate predicateToAdd, Predicate predicateWorkDay, CriteriaBuilder criteriaBuilder) {
        if (predicateWorkDay == null) {
            predicateWorkDay = predicateToAdd;
        }else {
            predicateWorkDay = criteriaBuilder.and(predicateWorkDay, predicateToAdd);
        }
    }

    private void appendToFinalPredicate(final Predicate predicateToAdd, Predicate finalPredicate, CriteriaBuilder criteriaBuilder) {
        if (finalPredicate == null) {
            finalPredicate = predicateToAdd;
        }else {
            finalPredicate = criteriaBuilder.and(finalPredicate, predicateToAdd);
        }
    }
}
