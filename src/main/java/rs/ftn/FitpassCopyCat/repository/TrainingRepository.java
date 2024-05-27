package rs.ftn.FitpassCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ftn.FitpassCopyCat.model.entity.Training;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {

    @Query("SELECT t FROM Training t WHERE t.user.id = :traineeId")
    List<Training> findAllByTrainee(@Param("traineeId") Long traineeId);

    @Query("SELECT COUNT(1) FROM Training t WHERE t.facility.id = :facilityId AND :wantedFrom > t.fromHours AND :wantedUntil < t.untilHours")
    Integer countOverlappingTrainings(@Param("wantedFrom") LocalDateTime fromHours, @Param("wantedUntil") LocalDateTime untilHours, @Param("facilityId") Long facilityId);
}
