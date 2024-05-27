package rs.ftn.FitpassCopyCat.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ftn.FitpassCopyCat.model.entity.Review;
import rs.ftn.FitpassCopyCat.repository.ReviewRepository;
import rs.ftn.FitpassCopyCat.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {
    private ReviewRepository reviewRepository;
    @Autowired
    public ReviewServiceImpl(ReviewRepository repository) {
        this.reviewRepository = repository;
    }

    @Override
    public void save(Review review) {
        reviewRepository.save(review);
    }
}
