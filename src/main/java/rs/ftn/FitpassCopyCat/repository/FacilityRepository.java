package rs.ftn.FitpassCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ftn.FitpassCopyCat.model.entity.Facility;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {

//    @Query("select f from Facility f where f.active = true")
    List<Facility> findByActiveTrue();

    @Query("SELECT f " +
            "FROM Facility f, Review rv, Comment c " +
            "WHERE " +
            "c.id = :commentId AND " +
            "c.id = rv.comment.id AND " +
            "rv.facility.id = f.id")
    Facility findByCommentLeft(@Param("commentId") Long commentId);
}
