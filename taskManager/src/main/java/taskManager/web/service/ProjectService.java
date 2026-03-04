package taskManager.web.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import taskManager.database.entity.ProjectEntity;
import taskManager.database.entity.UserEntity;
import taskManager.database.repository.ProjectRepository;
import taskManager.database.repository.UserRepository;
import taskManager.mapper.ProjectMapper;
import taskManager.web.dto.Project;
import taskManager.web.dto.ProjectResponse;

@Service
@RequiredArgsConstructor
public class ProjectService {
    

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;


    @Transactional
    public ProjectResponse createProject(Long currentUserId, Project projectRequest){

        UserEntity user = userRepository.findById(currentUserId).orElseThrow(
            () -> new IllegalArgumentException("User doesn't exist with id = " + currentUserId)
        );


        if (projectRepository.existsByOwnerIdAndName(currentUserId,projectRequest.name())){
            throw new IllegalArgumentException("Project with this name already exist");
        }
        

        ProjectEntity projectEntity = projectMapper.toEntity(projectRequest);
        projectEntity.setOwner(user);
        projectEntity.getMembersList().add(user);


        ProjectEntity saved = projectRepository.save(projectEntity);
        user.getOwnProjectsList().add(saved);
        user.getParticipatedProjectsList().add(saved);

        return projectMapper.toResponse(saved);
    }


    public ProjectResponse getProject(Long currentUserId, Long projectId){

        //member
        if (!projectRepository.existsByProjectIdAndMemberId(projectId, currentUserId)){
            throw new IllegalArgumentException("You are not member of this project or project doesn't exist with id = " + projectId);  
        }

        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(
            () -> new IllegalArgumentException("Project doesn't exist with id = " + projectId)
        );

        return projectMapper.toResponse(project);
    } 

}
