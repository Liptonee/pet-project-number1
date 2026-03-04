package taskManager.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import taskManager.web.dto.Comment;
import taskManager.web.dto.CommentResponse;
import taskManager.web.security.CustomUsersDetails;
import taskManager.web.service.CommentService;


@RestController
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;


    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<CommentResponse> createComment(
                                                   @AuthenticationPrincipal CustomUsersDetails currentUser,
                                                   @PathVariable("taskId") Long taskId,
                                                   @Valid @RequestBody Comment request) {

        CommentResponse response = commentService.createComment(currentUser.user().getId(), taskId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }

    
    //из задачи получить конкретный коммент
    @GetMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> getCommentFromTask(@AuthenticationPrincipal CustomUsersDetails currentUser,
                                                    @PathVariable("taskId") Long taskId,
                                                    @PathVariable("commentId") Long commentId){
        CommentResponse response = commentService.getCommentFromTask(currentUser.user().getId(), taskId, commentId);
        return ResponseEntity.ok(response);
    }


    //из профиля получить свой конкретный коммент
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> getComment(@AuthenticationPrincipal CustomUsersDetails currentUser,
                                                    @PathVariable("commentId") Long commentId){
        CommentResponse response = commentService.getComment(currentUser.user().getId(), commentId);
        return ResponseEntity.ok(response);
    }
}
