package rs.ftn.FitpassCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingCreateDTO {
    @NotBlank
    private LocalDateTime fromHours;
    @NotBlank
    private LocalDateTime untilHours;
    @NotBlank
    private Long facilityId;
}
