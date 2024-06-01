package rs.ftn.FitpassCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.WorkDay;

import java.time.DayOfWeek;

@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Long> {

    WorkDay findByDayAndFacility(DayOfWeek day, Facility facility);
}
