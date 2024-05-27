package rs.ftn.FitpassCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.entity.WorkDay;

import javax.validation.constraints.NotBlank;
import java.time.DayOfWeek;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkDayDTO {
    @NotBlank
    private DayOfWeek day;
    @NotBlank
    private LocalTime from;
    @NotBlank
    private LocalTime until;

    public WorkDayDTO(WorkDay workDay) {
        this.day = workDay.getDay();
        this.from = workDay.getFromHours();
        this.until = workDay.getUntilHours();
    }
}
