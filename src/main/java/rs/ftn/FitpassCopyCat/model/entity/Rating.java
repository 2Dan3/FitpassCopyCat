package rs.ftn.FitpassCopyCat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.DTO.RatingDTO;

import javax.persistence.*;

@Entity
@Table(name = "rating")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rating_id", nullable = false, unique = true)
    private Long id;
    @Column
    private Integer equipment;
    @Column
    private Integer staff;
    @Column
    private Integer hygiene;
    @Column
    private Integer space;

    public Rating(RatingDTO ratingData) {
        this.equipment = ratingData.getEquipment();
        this.staff = ratingData.getStaff();
        this.hygiene = ratingData.getHygiene();
        this.space = ratingData.getSpace();
    }

//    @OneToMany(mappedBy = "rating", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE,   CascadeType.REFRESH})
//    private Set<Review> reviews = new HashSet<Review>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Image i = (Image) o;
        return id != null && id.equals(i.getId());
    }

        @Override
        public int hashCode() {
        return 7111;
    }
}