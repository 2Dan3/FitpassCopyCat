package rs.ftn.FitpassCopyCat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.DTO.CommentCreateDTO;
import rs.ftn.FitpassCopyCat.model.DTO.RatingCreateDTO;
import rs.ftn.FitpassCopyCat.model.DTO.ReviewCreateDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false, unique = true)
    private Long id;
    @Column
    private LocalDateTime createdAt;
    @Column
    private Integer trainingCount;
    @Column
    private Boolean hidden;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rating_id")
    private Rating rating;

//    todo check Lazy -> Eager if needed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

//    todo check Lazy -> Eager if needed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Review r = (Review) o;
        return id != null && id.equals(r.getId());
    }

    @Override
    public int hashCode() {
        return 8111;
    }

    public Review(ReviewCreateDTO reviewData, Facility facilityReviewed,
                  User author, int trainingsDone) {

        final LocalDateTime creationTimestamp = LocalDateTime.now();

        CommentCreateDTO commentData = reviewData.getCommentData();
        if (commentData != null) {
            Comment newComment = new Comment(commentData, author, creationTimestamp);
            this.comment = newComment;
        }
        RatingCreateDTO ratingData = reviewData.getRatingData();
        this.rating = new Rating(ratingData);
        this.author = author;
        this.facility = facilityReviewed;
        trainingCount = trainingsDone;
        hidden = false;
        createdAt = creationTimestamp;
    }
}