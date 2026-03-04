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
import taskManager.web.dto.Comment;
import taskManager.web.dto.CommentResponse;
import taskManager.web.dto.Project;
import taskManager.web.dto.ProjectResponse;
import taskManager.web.dto.Task;
import taskManager.web.dto.TaskResponse;
import taskManager.web.dto.User;
import taskManager.web.dto.UserResponse;
import taskManager.web.security.CustomUsersDetails;
import taskManager.web.service.AppService;



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
                                                   @PathVariable("taskId") Long taskId,
                                                   @Valid @RequestBody Comment request) {

        CommentResponse response = appService.createComment(currentUser.user().getId(), taskId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }






    //получить данные профиля
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal CustomUsersDetails currentUser,
                                                @PathVariable("userId") Long userId){
    
        UserResponse response = appService.getUser(currentUser.user().getId(), userId);
        return ResponseEntity.ok(response);
    }
    
    //из профиля получить проект в котором ты участвуешь 
    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(@AuthenticationPrincipal CustomUsersDetails currentUser,
                                                    @PathVariable("projectId") Long projectId){
        ProjectResponse response = appService.getProject(currentUser.user().getId(), projectId);
        return ResponseEntity.ok(response);
    }

    //из профиля получить свою конкретную задачу
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@AuthenticationPrincipal CustomUsersDetails currentUser,
                                                    @PathVariable("taskId") Long taskId){
        TaskResponse response = appService.getTask(currentUser.user().getId(), taskId);
        return ResponseEntity.ok(response);
    }

    //из профиля получить свой конкретный коммент
    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> getComment(@AuthenticationPrincipal CustomUsersDetails currentUser,
                                                    @PathVariable("commentId") Long commentId){
        CommentResponse response = appService.getComment(currentUser.user().getId(), commentId);
        return ResponseEntity.ok(response);
    }


    //из проекта получить конкретную задачу 
    @GetMapping("projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskResponse> getTaskFromProject(@AuthenticationPrincipal CustomUsersDetails currentUser,
                                                    @PathVariable("projectId") Long projectId,
                                                    @PathVariable("taskId") Long taskId){
        TaskResponse response = appService.getTaskFromProject(currentUser.user().getId(), projectId, taskId);
        return ResponseEntity.ok(response);
    }


    //из задачи получить конкретный коммент
    @GetMapping("/tasks/{taskId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> getCommentFromTask(@AuthenticationPrincipal CustomUsersDetails currentUser,
                                                    @PathVariable("taskId") Long taskId,
                                                    @PathVariable("commentId") Long commentId){
        CommentResponse response = appService.getCommentFromTask(currentUser.user().getId(), taskId, commentId);
        return ResponseEntity.ok(response);
    }

}   
