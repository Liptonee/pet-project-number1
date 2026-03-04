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
import taskManager.web.dto.Project;
import taskManager.web.dto.ProjectResponse;
import taskManager.web.security.CustomUsersDetails;
import taskManager.web.service.ProjectService;


@RestController
@RequiredArgsConstructor
public class ProjectController {
    
    private final ProjectService projectService;



    @PostMapping("/projects")
    public ResponseEntity<ProjectResponse> createProject(
                                                @AuthenticationPrincipal CustomUsersDetails currentUser,
                                                @Valid @RequestBody Project request
    ) {
                                    
        ProjectResponse response = projectService.createProject(currentUser.user().getId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //из профиля получить проект в котором ты участвуешь 
    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(@AuthenticationPrincipal CustomUsersDetails currentUser,
                                                    @PathVariable("projectId") Long projectId){
        ProjectResponse response = projectService.getProject(currentUser.user().getId(), projectId);
        return ResponseEntity.ok(response);
    }

}
