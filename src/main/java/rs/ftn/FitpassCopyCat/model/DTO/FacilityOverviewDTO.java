package rs.ftn.FitpassCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.entity.Facility;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class FacilityOverviewDTO {
    private String address;
    private String city;
    private String name;

    public FacilityOverviewDTO(Facility facility) {
        name = facility.getName();
        city = facility.getCity();
        address = facility.getAddress();
    }
}
