package taskManager.web.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taskManager.database.entity.ProjectEntity;
import taskManager.database.entity.QProjectEntity;
import taskManager.database.entity.UserEntity;
import taskManager.database.repository.ProjectRepository;
import taskManager.database.repository.UserRepository;
import taskManager.mapper.PageMapper;
import taskManager.mapper.ProjectMapper;
import taskManager.web.dto.PageResponse;
import taskManager.web.dto.Project;
import taskManager.web.dto.ProjectResponse;
import taskManager.web.exception.DuplicateResourceException;
import taskManager.web.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {
    

    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final UserRepository userRepository;
    private final PageMapper pageMapper;

    @Transactional
    public ProjectResponse createProject(Long currentUserId, Project projectRequest
    ){
        log.info("From SERVICE called createProject");

        UserEntity user = userRepository.findById(currentUserId).orElseThrow(
            () -> new ResourceNotFoundException("User doesn't exist with id = " + currentUserId)
        );


        if (projectRepository.existsByOwnerIdAndName(currentUserId,projectRequest.name())){
            throw new DuplicateResourceException("Project with this name already exist");
        }
        

        ProjectEntity projectEntity = projectMapper.toEntity(projectRequest);
        projectEntity.setOwner(user);
        projectEntity.getMembersList().add(user);


        ProjectEntity saved = projectRepository.save(projectEntity);
        user.getOwnProjectsList().add(saved);
        user.getParticipatedProjectsList().add(saved);

        return projectMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProjectResponse getProject(Long currentUserId, Long projectId
    ){
        log.info("From SERVICE called getProject");

        //member
        if (!projectRepository.existsById(projectId)){
            throw new ResourceNotFoundException("Project doesn't exist with id = " + projectId);  
        }
        if (!projectRepository.existsByIdAndMemberId(projectId, currentUserId)){
            throw new AccessDeniedException("You are not member of this project with id = " + projectId);  
        }

        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(
            () -> new ResourceNotFoundException("Project doesn't exist with id = " + projectId)
        );

        return projectMapper.toResponse(project);
    } 

    @Transactional(readOnly = true)
    public PageResponse<ProjectResponse> getAllProjects(Long currentUserId,
            Boolean isOwner,
            Pageable pageable
    ){
        log.info("From SERVICE called getAllProjects");
        
        QProjectEntity project = QProjectEntity.projectEntity;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(project.membersList.any().id.eq(currentUserId));
        if(isOwner != null){
            if (isOwner) builder.and(project.owner.id.eq(currentUserId));
            
            else builder.and(project.owner.id.ne(currentUserId));
        }

        Page<ProjectEntity> page = projectRepository.findAll(builder, pageable);
        return pageMapper.toPageResponse(page, projectMapper::toResponse);

    }

}
