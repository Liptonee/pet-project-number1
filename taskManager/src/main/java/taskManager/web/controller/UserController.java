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
import lombok.extern.slf4j.Slf4j;
import taskManager.web.dto.User;
import taskManager.web.dto.UserResponse;
import taskManager.web.security.CustomUsersDetails;
import taskManager.web.service.UserService;


@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    

    private final UserService userService;


    
    @PostMapping("/registration")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody User request
    ){
        log.info("From CONTROLLER called createUser");
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }


    //получить свои данные
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getProfile(@AuthenticationPrincipal CustomUsersDetails currentUser
    ){
        log.info("From CONTROLLER called getProfile");
        UserResponse response = userService.getProfile(currentUser.user().getId());
        return ResponseEntity.ok(response);
    }

    //получить данные профиля
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("userId") Long userId
    ){
        log.info("From CONTROLLER called getUser");
        UserResponse response = userService.getUser(currentUser.user().getId(), userId);
        return ResponseEntity.ok(response);
    }
}
