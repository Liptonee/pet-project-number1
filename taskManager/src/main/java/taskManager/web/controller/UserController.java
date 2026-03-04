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
import taskManager.web.dto.User;
import taskManager.web.dto.UserResponse;
import taskManager.web.security.CustomUsersDetails;
import taskManager.web.service.UserService;


@RestController
@RequiredArgsConstructor
public class UserController {
    

    private final UserService userService;


    
    @PostMapping("/registration")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody User request) {

        UserResponse response = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(response);
    }

    //получить данные профиля
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal CustomUsersDetails currentUser,
                                                @PathVariable("userId") Long userId){
    
        UserResponse response = userService.getUser(currentUser.user().getId(), userId);
        return ResponseEntity.ok(response);
    }
}
