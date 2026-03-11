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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taskManager.web.dto.request.AddUserRequest;
import taskManager.web.dto.request.Project;
import taskManager.web.dto.request.ProjectPatch;
import taskManager.web.dto.response.PageResponse;
import taskManager.web.dto.response.ProjectResponse;
import taskManager.web.security.CustomUsersDetails;
import taskManager.web.service.ProjectService;


@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Проекты", description = "Управление проектами")
public class ProjectController {
    
    private final ProjectService projectService;

    @Operation(summary = "Создать проект",
    description = "Создаёт новый проект. Текущий пользователь становится владельцем и первым участником.")
    @PostMapping("/projects")
    public ResponseEntity<ProjectResponse> createProject(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @Valid @RequestBody Project request
    ){
        log.info("From CONTROLLER called createProject");   
        ProjectResponse response = projectService.createProject(currentUser.user().getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @Operation(summary = "Получить проект",
    description = "Возвращает данные проекта по его ID. Доступно участникам проекта.") 
    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("projectId") Long projectId
    ){
        log.info("From CONTROLLER called getProject"); 
        ProjectResponse response = projectService.getProject(currentUser.user().getId(), projectId);
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "Получить все проекты",
    description = "Возвращает список проектов, в которых участвует текущий пользователь. Можно отфильтровать по роли владельца (isOwner).")
    @GetMapping("/projects")
    public ResponseEntity<PageResponse<ProjectResponse>> getAllProjects(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @RequestParam(required=false) Boolean isOwner,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        log.info("From CONTROLLER called getAllProjects"); 
        PageResponse<ProjectResponse> response = projectService.getAllProjects(currentUser.user().getId(), isOwner, pageable);
        return ResponseEntity.ok(response);
    }
    


    @Operation(summary = "Полностью обновить проект",
    description = "Заменяет данные проекта. Доступно только владельцу.")
    @PutMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponse> putProject(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("projectId") Long projectId,
            @Valid @RequestBody Project request
    ){
        log.info("From CONTROLLER called putProject");
        ProjectResponse response = projectService.putProject(currentUser.user().getId(), projectId, request);
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "Частично обновить проект",
    description = "Обновляет отдельные поля проекта. Доступно только владельцу.")
    @PatchMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("projectId") Long projectId,
            @Valid @RequestBody ProjectPatch request
    ){
        log.info("From CONTROLLER called updateProject");
        ProjectResponse response = projectService.updateProject(currentUser.user().getId(), projectId, request);
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "Добавить участника",
    description = "Добавляет пользователя в проект. Доступно только владельцу.")
    @PostMapping("/projects/{projectId}/members")
    public ResponseEntity<Void> addMemberToProject(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("projectId") Long projectId,
            @Valid @RequestBody AddUserRequest request
    ){
        log.info("From CONTROLLER called addMemberToProject");
        projectService.addMemberToProject(currentUser.user().getId(), projectId, request.userId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    @Operation(summary = "Удалить проект",
    description = "Удаляет проект и все связанные задачи и комментарии (каскадно). Доступно только владельцу.")
    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("projectId") Long projectId
    ){
        log.info("From CONTROLLER called deleteProject");
        projectService.deleteProject(currentUser.user().getId(), projectId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Удалить участника",
    description = "Исключает пользователя из проекта. Доступно только владельцу.")
    @DeleteMapping("/projects/{projectId}/members/{userId}")
    public ResponseEntity<Void> deleteMember(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("projectId") Long projectId,
            @PathVariable("userId") Long userId
    ){
        log.info("From CONTROLLER called deleteMember");
        projectService.deleteMember(currentUser.user().getId(), projectId, userId);
        return ResponseEntity.noContent().build();
    }

}
