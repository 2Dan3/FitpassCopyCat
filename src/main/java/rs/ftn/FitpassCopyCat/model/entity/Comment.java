package rs.ftn.FitpassCopyCat.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ftn.FitpassCopyCat.model.DTO.CommentCreateDTO;

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

//    todo might need to change to EAGER
    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @ManyToOne()
    private Comment parentComment;

    public Comment(CommentCreateDTO commentData, User author, LocalDateTime creationTimestamp) {
        this.text = commentData.getText();
        this.createdAt = creationTimestamp;
        this.author = author;
//        this.parentComment =
    }

    public Comment(CommentCreateDTO commentData, User author, Comment parentComment) {
        this.text = commentData.getText();
        this.author = author;
        this.parentComment = parentComment;
        this.createdAt = LocalDateTime.now();
    }

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