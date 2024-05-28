package rs.ftn.FitpassCopyCat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.Review;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.repository.ReviewRepository;
import rs.ftn.FitpassCopyCat.service.ReviewService;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository reviewRepository;
    @Autowired
    public ReviewServiceImpl(ReviewRepository repository) {
        this.reviewRepository = repository;
    }

    @Override
    public void save(Review review) {
        reviewRepository.saveAndFlush(review);
    }

    @Override
    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findByAuthorAndRemovedFalse(user);
    }

    @Override
    public List<Review> getReviewsOnFacility(Facility facility) {
        return reviewRepository.findByFacilityAndRemovedFalse(facility);
    }

    @Override
    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }
}
