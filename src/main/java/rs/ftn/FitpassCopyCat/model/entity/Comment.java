package rs.ftn.FitpassCopyCat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false, unique = true)
    private Long id;
    @Column(nullable = false)
    private String text;
    @Column
    private LocalDateTime createdAt;

//    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
//    private Set<Review> reviews = new HashSet<Review>();

    @ManyToOne(fetch = FetchType.EAGER)
    private User author;

    @ManyToOne()
    private Comment parentComment;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Comment c = (Comment) o;
        return id != null && id.equals(c.getId());
    }

    @Override
    public int hashCode() {
        return 2111;
    }
}