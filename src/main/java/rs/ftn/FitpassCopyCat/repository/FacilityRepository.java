package rs.ftn.FitpassCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.ftn.FitpassCopyCat.model.entity.Facility;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

//    @Query("select f from Facility f where f.active = true")
    List<Facility> getAllActive();
}
