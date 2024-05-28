package rs.ftn.FitpassCopyCat.service;

import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.Review;
import rs.ftn.FitpassCopyCat.model.entity.User;

import java.util.List;

public interface ReviewService {
    void save(Review review);

    List<Review> getReviewsByUser(User user);

    List<Review> getReviewsOnFacility(Facility facility);
}
