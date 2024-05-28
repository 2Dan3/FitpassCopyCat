package rs.ftn.FitpassCopyCat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.Review;
import rs.ftn.FitpassCopyCat.model.entity.User;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query(nativeQuery = true,
            value = "select ((avg(rt.equipment) + avg(rt.hygiene) + avg(rt.space) + avg(rt.staff)) / 4) \n" +
            "from facility fc, review rw, rating rt \n" +
            "join rw on rw.facility_id = fc.facility_id \n" +
            "join rt on rt.rating_id = rw.rating_id \n" +
            "where fc.facility_id = :facilityReviewedId " +
            "and rw.removed = false")
    Double findAverageReviewRating(Long facilityReviewedId);

//  todo
//    @Query("select r from Review r where r.author.id = :authorId")
    List<Review> findByAuthorAndRemovedFalse(User user);

//  todo
//    @Query("select r from Review r where r.facility.id = :facilityId")
    List<Review> findByFacilityAndRemovedFalse(Facility facility);
}
