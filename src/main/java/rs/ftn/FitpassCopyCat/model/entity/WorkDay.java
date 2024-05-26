package rs.ftn.FitpassCopyCat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.DTO.WorkDayDTO;
import rs.ftn.FitpassCopyCat.model.enums.DayOfWeek;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "work_day")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_day_id", nullable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private LocalDate validSince;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;
    @Column(nullable = false)
    private LocalTime from;
    @Column(nullable = false)
    private LocalTime until;

    @ManyToOne(fetch = FetchType.LAZY)
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
        WorkDay w = (WorkDay) o;
        return id != null && id.equals(w.getId());
    }

    @Override
    public int hashCode() {
        return 9111;
    }

    public WorkDay(WorkDayDTO wdDTO, Facility facility) {
        validSince = LocalDate.now();
        day = wdDTO.getDay();
        from = wdDTO.getFrom();
        until = wdDTO.getUntil();
        this.facility = facility;
    }
}