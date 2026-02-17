package taskManager;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;


@RestController
public class AppController {
    
    private final AppService appService;

    public AppController(AppService appService){
        this.appService = appService;
    }


    @PostMapping("/registration")
    public ResponseEntity<User> getMethodName(@Valid @RequestBody User userToCreate) {
        return ResponseEntity.status(HttpStatus.CREATED)
                            .body(appService.createUser(userToCreate));
    }
    
    

}   
