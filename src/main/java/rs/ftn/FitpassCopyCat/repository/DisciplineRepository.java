package rs.ftn.FitpassCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ftn.FitpassCopyCat.model.entity.Discipline;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {
}
