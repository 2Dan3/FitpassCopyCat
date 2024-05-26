package rs.ftn.FitpassCopyCat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.DTO.DisciplineDTO;
import rs.ftn.FitpassCopyCat.model.DTO.FacilityCreateDTO;
import rs.ftn.FitpassCopyCat.model.DTO.WorkDayDTO;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "facility")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "facility_id", nullable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private LocalDate createdAt;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String city;
    @Column
    private Double totalRating;
    @Column
    private Boolean active;

    @OneToMany(mappedBy = "facility", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<Discipline> discipline = new HashSet<>();

    @OneToMany(mappedBy = "facility", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private Set<WorkDay> workDays = new HashSet<>();

    @OneToMany(mappedBy = "facility", cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Image> images = new HashSet<>();

    @ManyToMany(mappedBy = "managedFacilities", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH } )
    private Set<User> managers = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Facility f = (Facility) o;
        return id != null && id.equals(f.getId());
    }

    @Override
    public int hashCode() {
        return 5111;
    }

    public Facility(FacilityCreateDTO facilityCreateDTO, User newManager) {
        name = facilityCreateDTO.getName();
        description = facilityCreateDTO.getDescription();
        createdAt = LocalDate.now();
        address = facilityCreateDTO.getAddress();
        city = facilityCreateDTO.getCity();
        active = true;

        for (DisciplineDTO d : facilityCreateDTO.getDisciplineDTO()) {
            Discipline dsc = new Discipline(d, this);
            discipline.add(dsc);
        }

        for (WorkDayDTO wdDTO : facilityCreateDTO.getWorkDaysDTO()) {
            WorkDay wd = new WorkDay(wdDTO, this);
            workDays.add(wd);
        }

        getManagers().add(newManager);
        newManager.getManagedFacilities().add(this);
    }
}