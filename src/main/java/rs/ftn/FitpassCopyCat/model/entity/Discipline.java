package rs.ftn.FitpassCopyCat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.DTO.DisciplineCreateDTO;

import javax.persistence.*;

@Entity
@Table(name = "discipline")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Discipline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discipline_id", nullable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Discipline d = (Discipline) o;
        return id != null && id.equals(d.getId());
    }

    @Override
    public int hashCode() {
        return 3111;
    }

    public Discipline(DisciplineCreateDTO disciplineDTO, Facility facility) {
        this.name = disciplineDTO.getName();
        this.facility = facility;
    }
}