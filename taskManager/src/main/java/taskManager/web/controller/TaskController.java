package taskManager.web.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taskManager.database.entity.TaskPriority;
import taskManager.database.entity.TaskStatus;
import taskManager.web.dto.request.AddUserRequest;
import taskManager.web.dto.request.Task;
import taskManager.web.dto.request.TaskPatch;
import taskManager.web.dto.request.TaskStatusPatch;
import taskManager.web.dto.response.PageResponse;
import taskManager.web.dto.response.TaskResponse;
import taskManager.web.security.CustomUsersDetails;
import taskManager.web.service.TaskService;


@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Задачи", description = "Управление задачами")
public class TaskController {
    

    private final TaskService taskService;

    @Operation(summary = "Создать задачу",
    description = "Создаёт новую задачу в проекте. Доступно только владельцу проекта.")
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




    @Operation(summary = "Получить задачу",
    description = "Возвращает данные задачи по её ID. Доступно участникам проекта.")
    @GetMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> getTask(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("taskId") Long taskId
    ){
        log.info("From CONTROLLER called getTask");
        TaskResponse response = taskService.getTask(currentUser.user().getId(), taskId);
        return ResponseEntity.ok(response);
    }


    
    @Operation(summary = "Получить задачу из проекта",
    description = "Возвращает данные задачи, проверяя её принадлежность проекту. Доступно участникам проекта.")
    @GetMapping("/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<TaskResponse> getTaskFromProject(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("projectId") Long projectId,
            @PathVariable("taskId") Long taskId
    ){
        log.info("From CONTROLLER called getTaskFromProject");
        TaskResponse response = taskService.getTaskFromProject(currentUser.user().getId(), projectId, taskId);
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "Получить все свои задачи",
    description = "Возвращает список задач, где текущий пользователь является исполнителем. Можно фильтровать по статусу, приоритету и проекту.")
    @GetMapping("/tasks")
    public ResponseEntity<PageResponse<TaskResponse>> getAllTasks(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(required = false) Long projectId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        log.info("From CONTROLLER called getAllTasks");
        PageResponse<TaskResponse> response = taskService.getAllTasks(currentUser.user().getId(), status, priority, projectId, pageable);
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "Получить все задачи проекта",
    description = "Возвращает список задач указанного проекта. Можно фильтровать по статусу, приоритету и исполнителю. Доступно участникам проекта.")
    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<PageResponse<TaskResponse>> getAllTasksFromProject(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
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



    @Operation(summary = "Добавить исполнителя",
    description = "Назначает пользователя исполнителем задачи. Доступно только владельцу проекта.")
    @PostMapping("/tasks/{taskId}/executors")
    public ResponseEntity<Void> addExecutorToTask(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("taskId") Long taskId,
            @Valid @RequestBody AddUserRequest request
    ){
        log.info("From CONTROLLER called addExecutorToTask");
        taskService.addExecutorToTask(currentUser.user().getId(), taskId, request.userId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    @Operation(summary = "Изменить статус задачи",
    description = "Обновляет статус задачи. Участники могут переводить задачу в статус ON_REVIEW, владелец — в любой статус.")
    @PatchMapping("/tasks/{taskId}/status")
    public ResponseEntity<Void> addStatus(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("taskId") Long taskId,
            @Valid @RequestBody TaskStatusPatch request
    ){
        log.info("From CONTROLLER called addStatus");
        taskService.addStatus(currentUser.user().getId(), taskId, request);
        return ResponseEntity.ok().build();
    }



    @Operation(summary = "Обновить задачу",
    description = "Частично обновляет поля задачи (название, описание, дедлайн, приоритет). Доступно только владельцу проекта.")
    @PatchMapping("/tasks/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("taskId") Long taskId,
            @Valid @RequestBody TaskPatch request
    ){
        log.info("From CONTROLLER called addStatus");
        TaskResponse response = taskService.updateTask(currentUser.user().getId(), taskId, request);
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "Удалить задачу",
    description = "Удаляет задачу и все связанные комментарии. Доступно только владельцу проекта.")
    @DeleteMapping("/tasks/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("taskId") Long taskId
    ){
        log.info("From CONTROLLER called deleteTask");
        taskService.deleteTask(currentUser.user().getId(), taskId);
        return ResponseEntity.noContent().build();
    }



    @Operation(summary = "Удалить исполнителя",
    description = "Снимает пользователя с роли исполнителя задачи. Доступно только владельцу проекта.")
    @DeleteMapping("/tasks/{taskId}/members/{userId}")
    public ResponseEntity<Void> deleteExecutor(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("taskId") Long taskId,
            @PathVariable("userId") Long userId
    ){
        log.info("From CONTROLLER called deleteExecutor");
        taskService.deleteExecutor(currentUser.user().getId(), taskId, userId);
        return ResponseEntity.noContent().build();
    }

}
