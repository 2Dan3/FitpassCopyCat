package rs.ftn.FitpassCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ftn.FitpassCopyCat.model.entity.Training;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
}
