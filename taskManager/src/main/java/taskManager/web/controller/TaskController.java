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
import taskManager.database.entity.TaskPriority;
import taskManager.database.entity.TaskStatus;
import taskManager.web.dto.PageResponse;
import taskManager.web.dto.Task;
import taskManager.web.dto.TaskResponse;
import taskManager.web.security.CustomUsersDetails;
import taskManager.web.service.TaskService;


@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    

    private final TaskService taskService;




    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<TaskResponse> createTask(
                                                   @AuthenticationPrincipal CustomUsersDetails currentUser,
                                                   @PathVariable("projectId") Long projectId,
                                                   @Valid @RequestBody Task request
    ){
        log.info("From CONTROLLER called createTask");
        TaskResponse response = taskService.createTask(currentUser.user().getId(), projectId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //из профиля получить свою конкретную задачу
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> getTask(@AuthenticationPrincipal CustomUsersDetails currentUser,
                                                @PathVariable("taskId") Long taskId
    ){
        log.info("From CONTROLLER called getTask");
        TaskResponse response = taskService.getTask(currentUser.user().getId(), taskId);
        return ResponseEntity.ok(response);
    }


    //из проекта получить конкретную задачу 
    @GetMapping("/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskResponse> getTaskFromProject(@AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId
    ){
        log.info("From CONTROLLER called getTaskFromProject");
        TaskResponse response = taskService.getTaskFromProject(currentUser.user().getId(), projectId, taskId);
        return ResponseEntity.ok(response);
    }

    //получить все свои задачи
    @GetMapping("/tasks")
    public ResponseEntity<PageResponse<TaskResponse>> getAllTasks(@AuthenticationPrincipal CustomUsersDetails currentUser,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) Long projectId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        log.info("From CONTROLLER called getAllTasks");
        PageResponse<TaskResponse> response = taskService.getAllTasks(currentUser.user().getId(), status, priority, projectId, pageable);
        return ResponseEntity.ok(response);
    }


    //получить все задачи из проекта
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<PageResponse<TaskResponse>> getAllTasksFromProject(@AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("projectId") Long projectId,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) Long userId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        log.info("From CONTROLLER called getAllProjects");
        PageResponse<TaskResponse> response = taskService.getAllTasksFromProject(currentUser.user().getId(), projectId, status, priority, userId, pageable);
        return ResponseEntity.ok(response);
    }

}
