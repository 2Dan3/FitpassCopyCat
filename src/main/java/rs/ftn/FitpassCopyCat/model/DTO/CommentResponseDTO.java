package rs.ftn.FitpassCopyCat.model.DTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import rs.ftn.FitpassCopyCat.model.entity.Comment;

@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private Long id;
    private String text;
    private Long parentId;
    private String authorEmail;
    private boolean authorIsManager = false;

    public CommentResponseDTO(Comment newComment) {
        this.id = newComment.getId();
        this.text = newComment.getText();
        this.parentId = newComment.getParentComment().getId();
        this.authorEmail = newComment.getAuthor().getEmail();
    }
    public CommentResponseDTO(Comment newComment, boolean authorIsManager) {
        this.id = newComment.getId();
        this.text = newComment.getText();
        this.parentId = newComment.getParentComment().getId();
        this.authorEmail = newComment.getAuthor().getEmail();
        this.authorIsManager = authorIsManager;
    }
}