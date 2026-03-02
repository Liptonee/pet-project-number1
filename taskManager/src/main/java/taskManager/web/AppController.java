package taskManager.web;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import taskManager.web.dto.Comment;
import taskManager.web.dto.CommentResponse;
import taskManager.web.dto.Project;
import taskManager.web.dto.ProjectResponse;
import taskManager.web.dto.Task;
import taskManager.web.dto.TaskResponse;
import taskManager.web.dto.User;
import taskManager.web.dto.UserResponse;
import taskManager.web.security.CustomUsersDetails;


@RestController
public class AppController {
    
    private final AppService appService;

    public AppController(AppService appService){
        this.appService = appService;
    }


    @PostMapping("/registration")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody User request) {

        UserResponse response = appService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }
    
    
    @PostMapping("/projects")
    public ResponseEntity<ProjectResponse> createProject(
                                                @AuthenticationPrincipal CustomUsersDetails currentUser,
                                                @Valid @RequestBody Project request
    ) {
                                    
        ProjectResponse response = appService.createProject(currentUser.user().getId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    

    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskResponse> createTask(
                                                   @AuthenticationPrincipal CustomUsersDetails currentUser,
                                                   @PathVariable("projectId") Long projectId,
                                                   @Valid @RequestBody Task request) {

        TaskResponse response = appService.createTask(currentUser.user().getId(), projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/tasks/{taskId}/comments")
    public ResponseEntity<CommentResponse> createComment(
                                                   @AuthenticationPrincipal CustomUsersDetails currentUser,
                                                   @PathVariable("taskid") Long taskId,
                                                   @Valid @RequestBody Comment request) {

        CommentResponse response = appService.createComment(currentUser.user().getId(), taskId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }
}   
