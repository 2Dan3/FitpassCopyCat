package rs.ftn.FitpassCopyCat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column
    private String description;
    @Column
    private LocalDate createdAt;
    @Column
    private String address;
    @Column
    private String city;
    @Column
    private Double totalRating;
    @Column
    private Boolean active;

    @OneToMany(mappedBy = "facility", cascade = CascadeType.REMOVE)
    private Set<Discipline> discipline = new HashSet<Discipline>();

    @OneToMany(mappedBy = "facility", cascade = CascadeType.REMOVE)
    private Set<WorkDay> workDays = new HashSet<WorkDay>();

    @OneToMany(mappedBy = "facility", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private Set<Image> images = new HashSet<Image>();

    @ManyToMany(mappedBy = "managedFacilities", fetch = FetchType.EAGER, cascade = { CascadeType.REMOVE, CascadeType.REFRESH, CascadeType.DETACH } )
    private Set<User> managers = new HashSet<User>();

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
}