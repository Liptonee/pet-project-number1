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
import taskManager.web.dto.request.Project;
import taskManager.web.dto.request.ProjectPatch;
import taskManager.web.dto.response.PageResponse;
import taskManager.web.dto.response.ProjectResponse;
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


        if (projectRepository.existsByNameAndOwnerId(projectRequest.name(), currentUserId)){
            throw new DuplicateResourceException("Project with this name already exist");
        }
        

        ProjectEntity projectEntity = projectMapper.toEntity(projectRequest);
        projectEntity.setOwner(user);

        ProjectEntity saved = projectRepository.save(projectEntity);
        projectRepository.addMember(saved.getId(), currentUserId);

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


    @Transactional
    public ProjectResponse putProject(Long currentUserId, Long projectId, Project request
    ){
        log.info("From SERVICE called updateProject");


        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id = " + projectId));

        //owner
        if (!project.getOwner().getId().equals(currentUserId)) {
            throw new AccessDeniedException("You are not the owner of this project");
        }

        project.setName(request.name());
        project.setDescription(request.description());

        return projectMapper.toResponse(project);
    }


    @Transactional
    public void addMemberToProject(Long currentUserId, Long projectId, Long userId
    ){
        log.info("From SERVICE called addMemberToProject");
        //exist
        if(!projectRepository.existsById(projectId)){
            throw new ResourceNotFoundException("Project not found with id = " + projectId);
        }
        //owner
        if(!projectRepository.existsByIdAndOwnerId(projectId, currentUserId)){
            throw new AccessDeniedException("You are not the owner of this project");
        }
        //exist
        if(!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("User not found with id = " + userId);
        }
        //duplicate
        if(projectRepository.existsByIdAndMemberId(projectId, userId)){
            throw new DuplicateResourceException("User is already member");
        }

        projectRepository.addMember(projectId, userId);
    }

    @Transactional
    public ProjectResponse updateProject(Long currentUserId, Long projectId, ProjectPatch request
    ){
        log.info("From SERVICE called updateProject");

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id = " + projectId));

        if(!project.getOwner().getId().equals(currentUserId)){
            throw new AccessDeniedException("You are not the owner of this project");
        }

        if(request.name() != null){
            if(request.name().isBlank()){
                throw new IllegalArgumentException("Name cant be blank");
            }
            if(projectRepository.existsByNameAndOwnerId(request.name(), currentUserId)){
                throw new DuplicateResourceException("This name already in use");
            }
            project.setName(request.name());
        }

        if (request.description() != null) {
            project.setDescription(request.description().isEmpty() ? null : request.description());
        }

        return projectMapper.toResponse(project);
    }


    @Transactional
    public void deleteProject(Long currentUserId, Long projectId
    ){
        log.info("From SERVICE called updateProject");

        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        if (!project.getOwner().getId().equals(currentUserId)) {
            throw new AccessDeniedException("You are not the owner of this project");
        }
        projectRepository.deleteAllMembers(projectId);
        projectRepository.delete(project);
    }

    @Transactional
    public void deleteMember(Long currentUserId, Long projectId, Long userId
    ){
        log.info("From SERVICE called deleteMember");

        if(!projectRepository.existsById(projectId)){
            throw new ResourceNotFoundException("Project not found");
        }
        if (!projectRepository.existsByIdAndOwnerId(projectId, currentUserId)) {
            throw new AccessDeniedException("You are not the owner of this project");
        }
        if(!projectRepository.existsByIdAndMemberId(projectId, userId)){
            throw new IllegalArgumentException("User is not member of this project");
        }
        
        projectRepository.deleteMember(projectId,userId);
    }


}
