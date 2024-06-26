package rs.ftn.FitpassCopyCat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.DTO.TrainingCreateDTO;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "training")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "training_id", nullable = false, unique = true)
    private Long id;
    @Column
    private LocalDateTime fromHours;
    @Column
    private LocalDateTime untilHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    private Facility facility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Training(TrainingCreateDTO trainingData, Facility facility, User trainee) {
        fromHours = trainingData.getFromHours();
        untilHours = trainingData.getUntilHours();
        this.facility = facility;
        user = trainee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Training e = (Training) o;
        return id != null && id.equals(e.getId());
    }

    @Override
    public int hashCode() {
        return 4111;
    }
}