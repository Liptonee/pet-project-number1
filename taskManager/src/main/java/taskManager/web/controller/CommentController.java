package taskManager.web.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taskManager.web.dto.Comment;
import taskManager.web.dto.CommentResponse;
import taskManager.web.dto.PageResponse;
import taskManager.web.security.CustomUsersDetails;
import taskManager.web.service.CommentService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {
    
    private final CommentService commentService;


    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<CommentResponse> createComment(@AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("taskId") Long taskId,
            @Valid @RequestBody Comment request
    ){
        log.info("From CONTROLLER called createComment");
        CommentResponse response = commentService.createComment(currentUser.user().getId(), taskId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }

    
    //из задачи получить конкретный коммент
    @GetMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> getCommentFromTask(@AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("taskId") Long taskId,
            @PathVariable("commentId") Long commentId
    ){
        log.info("From CONTROLLER called getCommentFromTask");
        CommentResponse response = commentService.getCommentFromTask(currentUser.user().getId(), taskId, commentId);
        return ResponseEntity.ok(response);
    }


    //из профиля получить свой конкретный коммент
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> getComment(@AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("commentId") Long commentId
    ){
        log.info("From CONTROLLER called getComment");
        CommentResponse response = commentService.getComment(currentUser.user().getId(), commentId);
        return ResponseEntity.ok(response);
    }


    //из профиля получить все коменты
    @GetMapping("/comments")
    public ResponseEntity<PageResponse<CommentResponse>> getAllComments(@AuthenticationPrincipal CustomUsersDetails currentUser,
            @RequestParam(required=false) Long taskId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        log.info("From CONTROLLER called getAllComments");
        PageResponse<CommentResponse> response = commentService.getAllComments(currentUser.user().getId(), taskId, pageable);
        return ResponseEntity.ok(response);
    }




    // //из задачи получить все коменты
    @GetMapping("/tasks/{taskId}/comments")
    public ResponseEntity<PageResponse<CommentResponse>> getAllCommentsFromTask(@AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("taskId") Long taskId,
            @RequestParam(required=false) Long userId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        log.info("From CONTROLLER called getAllCommentsFromTask");
        PageResponse<CommentResponse> response = commentService.getAllCommentsFromTask(currentUser.user().getId(), taskId, userId, pageable);
        return ResponseEntity.ok(response);
    }
}
