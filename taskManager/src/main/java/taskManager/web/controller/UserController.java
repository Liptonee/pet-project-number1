package taskManager.web.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import taskManager.web.dto.request.PasswordPatch;
import taskManager.web.dto.request.User;
import taskManager.web.dto.request.UserPatch;
import taskManager.web.dto.response.PageResponse;
import taskManager.web.dto.response.UserResponse;
import taskManager.web.dto.response.UsernameResponse;
import taskManager.web.security.CustomUsersDetails;
import taskManager.web.service.UserService;


@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Пользователи", description = "Управление пользователями")
public class UserController {
    

    private final UserService userService;


    @Operation(summary = "Зарегистрировать пользователя",
    description = "Создаёт новую учётную запись. Доступно без аутентификации.")
    @PostMapping("/registration")
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody User request
    ){
        log.info("From CONTROLLER called createUser");
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }



    @Operation(summary = "Получить свой профиль",
    description = "Возвращает данные текущего аутентифицированного пользователя.")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getProfile(
            @AuthenticationPrincipal CustomUsersDetails currentUser
    ){
        log.info("From CONTROLLER called getProfile");
        UserResponse response = userService.getProfile(currentUser.user().getId());
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "Получить пользователя",
    description = "Возвращает данные пользователя. Если профиль приватный, доступ разрешён только самому пользователю.")
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(
        @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("userId") Long userId
    ){
        log.info("From CONTROLLER called getUser");
        UserResponse response = userService.getUser(currentUser.user().getId(), userId);
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "Получить участников проекта",
    description = "Возвращает список пользователей, участвующих в проекте. Можно отфильтровать по исполнителям конкретной задачи (taskId). Доступно участникам проекта.")
    @GetMapping("/projects/{projectId}/members")
    public ResponseEntity<PageResponse<UsernameResponse>> getAllUsersFromProject(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("projectId") Long projectId,
            @RequestParam(required=false) Long taskId,
            @PageableDefault(size = 20, sort = "username", direction = Sort.Direction.DESC) Pageable pageable
    ){
        log.info("From CONTROLLER called getAllUsersFromProject");
        PageResponse<UsernameResponse> response = userService.getAllUsersFromProject(currentUser.user().getId(), projectId, taskId, pageable);
        return ResponseEntity.ok(response);
    }
    


    @Operation(summary = "Получить исполнителей задачи",
    description = "Возвращает список пользователей, назначенных исполнителями задачи. Доступно участникам проекта.")
    @GetMapping("/tasks/{taskId}/executors")
    public ResponseEntity<PageResponse<UserResponse>> getAllUsersFromTask(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("taskId") Long taskId,
            @PageableDefault(size = 20, sort = "username", direction = Sort.Direction.DESC) Pageable pageable
    ){
        log.info("From CONTROLLER called getAllUsersFromTask");
        PageResponse<UserResponse> response = userService.getAllUsersFromTask(currentUser.user().getId(), taskId, pageable);
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "Обновить профиль",
    description = "Обновляет отдельные поля профиля (email, username, приватность). Проверяет уникальность email и username.")
    @PatchMapping("/me")
    public ResponseEntity<UserResponse> updateUser(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @RequestBody @Valid UserPatch request
    ){
        log.info("From CONTROLLER called updateUser");
        UserResponse response = userService.updateUser(currentUser.user().getId(), request);
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "Сменить пароль",
    description = "Изменяет пароль пользователя. Требуется указание старого пароля и подтверждение нового.")
    @PatchMapping("/me/password")
     public ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal CustomUsersDetails currentUser,
            @RequestBody @Valid PasswordPatch request
    ){
        log.info("From CONTROLLER called updatePassword");
        userService.updatePassword(currentUser.user().getId(), request);
        return ResponseEntity.ok().build();
    }
}
