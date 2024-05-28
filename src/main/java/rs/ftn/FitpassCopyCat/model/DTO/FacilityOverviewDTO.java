package rs.ftn.FitpassCopyCat.model.DTO;

import lombok.AllArgsConstructor;
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
    private Boolean active;

    public FacilityOverviewDTO(Facility facility) {
        name = facility.getName();
        city = facility.getCity();
        address = facility.getAddress();
        active = facility.getActive();
    }
}
