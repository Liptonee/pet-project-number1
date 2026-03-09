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
import taskManager.web.dto.PageResponse;
import taskManager.web.dto.Project;
import taskManager.web.dto.ProjectResponse;
import taskManager.web.security.CustomUsersDetails;
import taskManager.web.service.ProjectService;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ProjectController {
    
    private final ProjectService projectService;



    @PostMapping("/projects")
    public ResponseEntity<ProjectResponse> createProject(@AuthenticationPrincipal CustomUsersDetails currentUser,
            @Valid @RequestBody Project request
    ){
        log.info("From CONTROLLER called createProject");   
        ProjectResponse response = projectService.createProject(currentUser.user().getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //из профиля получить проект в котором ты участвуешь 
    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectResponse> getProject(@AuthenticationPrincipal CustomUsersDetails currentUser,
            @PathVariable("projectId") Long projectId
    ){
        log.info("From CONTROLLER called getProject"); 
        ProjectResponse response = projectService.getProject(currentUser.user().getId(), projectId);
        return ResponseEntity.ok(response);
    }


    //из профиля получить все свои проекты
    @GetMapping("/projects")
    public ResponseEntity<PageResponse<ProjectResponse>> getAllProjects(@AuthenticationPrincipal CustomUsersDetails currentUser,
            @RequestParam(required=false) Boolean isOwner,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ){
        log.info("From CONTROLLER called getAllProjects"); 
        PageResponse<ProjectResponse> response = projectService.getAllProjects(currentUser.user().getId(), isOwner, pageable);
        return ResponseEntity.ok(response);
    }    

}
