package rs.ftn.FitpassCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.entity.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FacilityResponseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate createdAt;
    private String address;
    private String city;
    private Double totalRating;
    private Boolean active;
    private List<String> managerEmails;
    private List<String> disciplineNames;
    private List<WorkDayDTO> workDayDTOS;
    private List<String> imagePaths;

    public FacilityResponseDTO(Facility facility) {
        this.id = facility.getId();
        this.name = facility.getName();
        this.description = facility.getDescription();
        this.createdAt = facility.getCreatedAt();
        this.address = facility.getAddress();
        this.city = facility.getCity();
        this.totalRating = facility.getTotalRating();
        this.active = facility.getActive();

        this.managerEmails = new ArrayList<>();
        for (User manager : facility.getManagers()) {
            this.managerEmails.add(manager.getEmail());
        }

        this.disciplineNames = new ArrayList<>();
        for (Discipline d : facility.getDisciplines()) {
            this.disciplineNames.add(d.getName());
        }

        this.workDayDTOS = new ArrayList<>();
        for (WorkDay workDay : facility.getWorkDays()) {
            workDayDTOS.add(new WorkDayDTO(workDay));
        }

        this.imagePaths = new ArrayList<>();
        for (Image i : facility.getImages()) {
            imagePaths.add(i.getPath());
        }
    }
}
