package rs.ftn.FitpassCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RatingCreateDTO {
    @NotBlank
    @Min(1)
    @Max(10)
    private Integer equipment;
    @NotBlank
    @Min(1)
    @Max(10)
    private Integer staff;
    @NotBlank
    @Min(1)
    @Max(10)
    private Integer hygiene;
    @NotBlank
    @Min(1)
    @Max(10)
    private Integer space;
}
