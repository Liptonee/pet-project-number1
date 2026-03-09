package taskManager.web.service;


import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taskManager.database.entity.ProjectEntity;
import taskManager.database.entity.QTaskEntity;
import taskManager.database.entity.TaskEntity;
import taskManager.database.entity.TaskPriority;
import taskManager.database.entity.TaskStatus;
import taskManager.database.repository.ProjectRepository;
import taskManager.database.repository.TaskRepository;
import taskManager.mapper.PageMapper;
import taskManager.mapper.TaskMapper;
import taskManager.web.dto.PageResponse;
import taskManager.web.dto.Task;
import taskManager.web.dto.TaskResponse;

@RequiredArgsConstructor
@Service
@Slf4j
public class TaskService {
    


    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;
    private final PageMapper pageMapper;

    @Transactional
    public TaskResponse createTask(Long currentUserId, Long projectId, Task request
    ){
        log.info("From SERVICE called createTask");

        if (!projectRepository.existsByOwnerIdAndId(currentUserId, projectId)){
            throw new IllegalArgumentException("User doesn't owner of this project with id = " + projectId);
        }   
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(
            () -> new IllegalArgumentException("Project doesn't exist with id = " + projectId)
        );

        if(request.deadline() != null){
            if (request.deadline().isAfter(LocalDateTime.now())) 
                throw new IllegalArgumentException("Deadline must be in future");
        }

        TaskEntity taskEntity = taskMapper.toEntity(request);
        taskEntity.setPriority(request.priority() == null ? TaskPriority.NOT_SELECTED : request.priority());
        taskEntity.setDescription(request.description() == null ? "No description" : request.description());
        taskEntity.setStatus(TaskStatus.IN_PROCESS);
        taskEntity.setProject(project);

        TaskEntity saved = taskRepository.save(taskEntity);
        project.getTasksList().add(saved);

        return taskMapper.toResponse(saved);
    }


    @Transactional(readOnly = true)
    public TaskResponse getTask(Long currentUserId, Long taskId
    ){
        log.info("From SERVICE called getTask");
        
        //exist
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
            () -> new IllegalArgumentException("Task doesn't exist with id = " + taskId)
        );
        
        //member
        if(!projectRepository.existsByIdAndMemberId(task.getProject().getId(), currentUserId)){
            throw new IllegalArgumentException("You are not member of this project or project doesn't exist");
        }

        return taskMapper.toResponse(task);

    } 

    @Transactional(readOnly = true)
    public TaskResponse getTaskFromProject(Long currentUserId, Long projectId, Long taskId
    ){
        log.info("From SERVICE called getTaskFromProject");

        //member
        if (!projectRepository.existsById(projectId)){
            throw new IllegalArgumentException("Project doesn't exist with id = " + projectId);  
        }
        if (!projectRepository.existsByIdAndMemberId(projectId, currentUserId)){
            throw new IllegalArgumentException("You are not member of this project with id = " + projectId);  
        }


        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
            () -> new IllegalArgumentException("Task doesn't exist with id = " + taskId)
        );

        //task in this project
        if (!task.getProject().getId().equals(projectId)){
            throw new IllegalArgumentException("Task doesn't belong to this project");
        }

        return taskMapper.toResponse(task);
    }

    @Transactional(readOnly = true)
    public PageResponse<TaskResponse> getAllTasks(Long currentUserId,
            TaskStatus status,
            TaskPriority priority,
            Long projectId,
            Pageable pageable
    ){
        log.info("From SERVICE called getAllTasks");

        QTaskEntity task = QTaskEntity.taskEntity;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(task.executorsList.any().id.eq(currentUserId));

        if (status != null) builder.and(task.status.eq(status));
        if (priority != null) builder.and(task.priority.eq(priority));    
        if (projectId != null) builder.and(task.project.id.eq(projectId));

        Page<TaskEntity> page = taskRepository.findAll(builder, pageable);
        return pageMapper.toPageResponse(page, taskMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public PageResponse<TaskResponse> getAllTasksFromProject(Long currentUserId,
            Long projectId,
            TaskStatus status,
            TaskPriority priority,
            Long userId,
            Pageable pageable
    ){

        log.info("From SERVICE called getAllTasksFromProject");

        //member
        if (!projectRepository.existsById(projectId)){
            throw new IllegalArgumentException("Project doesn't exist with id = " + projectId);  
        }
        if (!projectRepository.existsByIdAndMemberId(projectId, currentUserId)){
            throw new IllegalArgumentException("You are not member of this project with id = " + projectId);  
        }

        QTaskEntity task = QTaskEntity.taskEntity;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(task.project.id.eq(projectId));

        if (status != null) builder.and(task.status.eq(status));
        if (priority != null) builder.and(task.priority.eq(priority));
        if (userId != null) builder.and(task.executorsList.any().id.eq(userId));

        Page<TaskEntity> page = taskRepository.findAll(builder, pageable);
        return pageMapper.toPageResponse(page, taskMapper::toResponse);
    }
}   
