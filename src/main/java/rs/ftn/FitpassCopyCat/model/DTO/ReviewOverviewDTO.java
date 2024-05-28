package rs.ftn.FitpassCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.entity.Review;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReviewOverviewDTO {
    private Long id;
    private LocalDateTime createdAt;
    private Boolean hidden;
    private String authorEmail;
    private String authorImagePath;
    private String commentText;
    private RatingDTO ratingData;
    private FacilityOverviewDTO facilityData;

    public ReviewOverviewDTO(Review r) {
        this.id = r.getId();
        this.createdAt = r.getCreatedAt();
        this.hidden = r.getHidden();
        this.authorEmail = r.getAuthor().getEmail();
        this.authorImagePath = r.getAuthor().getImage() == null ? null : r.getAuthor().getImage().getPath();
        this.commentText = r.getComment().getText();
        this.ratingData = new RatingDTO(r.getRating().getEquipment(), r.getRating().getStaff(), r.getRating().getHygiene(), r.getRating().getSpace());
        this.facilityData = new FacilityOverviewDTO(r.getFacility());
    }
}
