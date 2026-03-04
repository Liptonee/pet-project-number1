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
import taskManager.web.dto.Task;
import taskManager.web.dto.TaskResponse;
import taskManager.web.security.CustomUsersDetails;
import taskManager.web.service.TaskService;


@RestController
@RequiredArgsConstructor
public class TaskController {
    

    private final TaskService taskService;




    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskResponse> createTask(
                                                   @AuthenticationPrincipal CustomUsersDetails currentUser,
                                                   @PathVariable("projectId") Long projectId,
                                                   @Valid @RequestBody Task request) {

        TaskResponse response = taskService.createTask(currentUser.user().getId(), projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //из профиля получить свою конкретную задачу
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@AuthenticationPrincipal CustomUsersDetails currentUser,
                                                    @PathVariable("taskId") Long taskId){
        TaskResponse response = taskService.getTask(currentUser.user().getId(), taskId);
        return ResponseEntity.ok(response);
    }


    //из проекта получить конкретную задачу 
    @GetMapping("projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskResponse> getTaskFromProject(@AuthenticationPrincipal CustomUsersDetails currentUser,
                                                    @PathVariable("projectId") Long projectId,
                                                    @PathVariable("taskId") Long taskId){
        TaskResponse response = taskService.getTaskFromProject(currentUser.user().getId(), projectId, taskId);
        return ResponseEntity.ok(response);
    }

}
