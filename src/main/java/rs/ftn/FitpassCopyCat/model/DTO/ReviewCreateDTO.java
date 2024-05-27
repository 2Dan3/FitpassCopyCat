package rs.ftn.FitpassCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCreateDTO {
    @NotBlank
    private String authorEmail;
    @NotBlank
    private Long facilityId;
//    @NotBlank
    private RatingCreateDTO ratingData;
    private CommentCreateDTO commentData;
}