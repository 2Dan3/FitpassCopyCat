package rs.ftn.FitpassCopyCat.service;

import rs.ftn.FitpassCopyCat.model.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment findById(Long id);

    Comment save(Comment comment);

    List<Comment> findRepliesTo(Comment parentComment);
}
