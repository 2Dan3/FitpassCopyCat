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

    @Query("select ((avg(rt.equipment) + avg(rt.hygiene) + avg(rt.space) + avg(rt.staff)) / 4)\n" +
            "from Facility fc\n" +
            "join Review rw on rw.facility.id = fc.id\n" +
            "join Rating rt on rt.id = rw.rating.id\n" +
            "where fc.id = :facilityReviewedId" +
            "and rw.removed = false")
    Double findAverageReviewRating(Long facilityReviewedId);

//  todo
//    @Query("select r from Review r where r.author.id = :authorId")
    List<Review> getAllByUserAndIsNotRemoved(User user);

//  todo
//    @Query("select r from Review r where r.facility.id = :facilityId")
    List<Review> getAllByFacilityAndIsNotRemoved(Facility facility);
}
