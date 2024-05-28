package rs.ftn.FitpassCopyCat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rs.ftn.FitpassCopyCat.model.DTO.CommentCreateDTO;
import rs.ftn.FitpassCopyCat.model.DTO.CommentResponseDTO;
import rs.ftn.FitpassCopyCat.model.DTO.FacilityOverviewDTO;
import rs.ftn.FitpassCopyCat.model.entity.Comment;
import rs.ftn.FitpassCopyCat.model.entity.Facility;
import rs.ftn.FitpassCopyCat.model.entity.User;
import rs.ftn.FitpassCopyCat.service.CommentService;
import rs.ftn.FitpassCopyCat.service.FacilityService;
import rs.ftn.FitpassCopyCat.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "${apiPrefix}/comments")
public class CommentReplyController {
    private CommentService commentService;
    private UserService userService;
    private FacilityService facilityService;
    @Autowired
    public CommentReplyController(CommentService commentService, UserService userService,
                                  FacilityService facilityService) {
        this.commentService = commentService;
        this.userService = userService;
        this.facilityService = facilityService;
    }


    @GetMapping(path = "/{commentId}/replies")
    public ResponseEntity<List<CommentResponseDTO>> getAllReplies(@PathVariable(name = "commentId") Long originalCommentId) {
        Comment parentComment = commentService.findById(originalCommentId);
        if (parentComment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Facility facility = facilityService.findByCommentLeft(parentComment.getId());

        List<CommentResponseDTO> commentResponseDTOs = new ArrayList<>();
        List<Comment> repliesFound = commentService.findRepliesTo(parentComment);
        boolean authoredByManager;

        for (Comment c : repliesFound) {

            authoredByManager = userService.managesFacility(facility.getId(), c.getAuthor());
            commentResponseDTOs.add(new CommentResponseDTO(c, authoredByManager));
        }

        return new ResponseEntity<>(commentResponseDTOs, HttpStatus.OK);
    }

    @PostMapping(path = "/{commentId}")
    public ResponseEntity<CommentResponseDTO> replyToComment(@Valid @RequestBody CommentCreateDTO commentData, @PathVariable(name = "commentId") Long parentCommentId, Authentication authentication) {
        Comment parentComment = commentService.findById(parentCommentId);
        if (parentComment == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        User commenter = userService.findByEmail(authentication.getName());
        Facility facility = facilityService.findByCommentLeft(parentComment.getId());

        if (userService.managesFacility(facility.getId(), commenter)) {
//            Commenter: Manager in this facility

            Comment newReply = new Comment(commentData, commenter, parentComment);

            return new ResponseEntity<>(new CommentResponseDTO(commentService.save(newReply), true), HttpStatus.OK);

        }else {
//            Commenter: Regular user

//            todo - might need to change if condition :
//             Replying to Manager -> only Replying to manager who replied to MY comment (og)
            if ( ! userService.managesFacility(facility.getId(), parentComment.getAuthor()) )
//            Unauthorized reply to regular user's comment by a regular user
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


//            Else: Allow reply to a Manager's comment

            Comment newReply = new Comment(commentData, commenter, parentComment);

            return new ResponseEntity<>(new CommentResponseDTO(commentService.save(newReply)), HttpStatus.OK);
        }
    }
}
