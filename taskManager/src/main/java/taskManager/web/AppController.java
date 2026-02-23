package taskManager.web;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import taskManager.web.dto.User;
import taskManager.web.dto.UserResponse;


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
    
    
    // @PostMapping("/{userId}/project")
    // public ResponseEntity<ProjectResponse> createProject
    // (                                          @Valid @RequestBody Project request,
    //                                             @PathVariable("userId") Long userId
    // ){

    //     ProjectResponse response = appService.createProject(request,userId);

    //     return ResponseEntity.status(HttpStatus.CREATED)
    //                          .body(response);    
    // }
}   
