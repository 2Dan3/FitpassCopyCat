package rs.ftn.FitpassCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rs.ftn.FitpassCopyCat.model.DTO.ReviewCreateDTO;
import rs.ftn.FitpassCopyCat.model.DTO.ReviewOverviewDTO;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.Review;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.service.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/reviews")
public class ReviewController {
    private ReviewService reviewService;
    private FacilityService facilityService;
    private TrainingService trainingService;
    private CommentService commentService;
    private UserService userService;
    @Autowired
    public ReviewController(ReviewService reviewService, FacilityService facilityService,
                            TrainingService trainingService, CommentService commentService,
                            UserService userService) {
        this.reviewService = reviewService;
        this.facilityService = facilityService;
        this.trainingService = trainingService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Void> writeReview(@Valid @RequestBody ReviewCreateDTO reviewData, Authentication authentication) {
        Facility facilityReviewed = facilityService.findById(reviewData.getFacilityId());
        if (facilityReviewed == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User loggedReviewer = userService.findByEmail(authentication.getName());
        if (loggedReviewer == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        int reviewerTrainingCountInFacility = trainingService.countTrainings(loggedReviewer, facilityReviewed);
        if (reviewerTrainingCountInFacility == 0)
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

        Review unsavedReview = new Review(reviewData, facilityReviewed, loggedReviewer, reviewerTrainingCountInFacility);
        /*Review newReview = */reviewService.save(unsavedReview);
        facilityService.updateFacilityRating(facilityReviewed);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReviewOverviewDTO>> getOwnReviews(Authentication authentication) {
        User loggedUser = userService.findByEmail(authentication.getName());
        if (loggedUser == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        List<ReviewOverviewDTO> reviewDTOs = new ArrayList<>();
        List<Review> reviewsWritten = reviewService.getReviewsByUser(loggedUser);
        for (Review r : reviewsWritten) {
            reviewDTOs.add(new ReviewOverviewDTO(r));
        }

        return new ResponseEntity<>(reviewDTOs, HttpStatus.OK);
    }
}
