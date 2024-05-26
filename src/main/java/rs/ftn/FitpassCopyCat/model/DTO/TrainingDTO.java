package rs.ftn.FitpassCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.entity.Training;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingDTO {
    private Long id;
    private LocalDateTime fromHours;
    private LocalDateTime untilHours;
    private String facilityName;
    private String facilityAddress;
    private String facilityCity;

    public TrainingDTO(Training training) {
        id = training.getId();
        fromHours = training.getFromHours();
        untilHours = training.getUntilHours();
        facilityName = training.getFacility().getName();
        facilityAddress = training.getFacility().getAddress();
        facilityCity = training.getFacility().getCity();
    }
}
