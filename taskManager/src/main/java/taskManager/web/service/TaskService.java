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
import taskManager.database.entity.QTaskEntity;
import taskManager.database.entity.TaskEntity;
import taskManager.database.entity.TaskPriority;
import taskManager.database.entity.TaskStatus;
import taskManager.database.repository.ProjectRepository;
import taskManager.database.repository.TaskRepository;
import taskManager.database.repository.UserRepository;
import taskManager.mapper.PageMapper;
import taskManager.mapper.TaskMapper;
import taskManager.web.dto.request.Task;
import taskManager.web.dto.request.TaskPatch;
import taskManager.web.dto.request.TaskStatusPatch;
import taskManager.web.dto.response.PageResponse;
import taskManager.web.dto.response.TaskResponse;
import taskManager.web.exception.DuplicateResourceException;
import taskManager.web.exception.ResourceNotFoundException;

@RequiredArgsConstructor
@Service
@Slf4j
public class TaskService {
    


    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;
    private final PageMapper pageMapper;
    private final UserRepository userRepository;

    @Transactional
    public TaskResponse createTask(Long currentUserId, Long projectId, Task request
    ){
        log.info("From SERVICE called createTask");

        //owner
        if (!projectRepository.existsByIdAndOwnerId(projectId, currentUserId)){
            throw new AccessDeniedException("You doesn't owner of this project with id = " + projectId);
        }   
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(
            () -> new ResourceNotFoundException("Project doesn't exist with id = " + projectId)
        );

        TaskEntity taskEntity = taskMapper.toEntity(request);
        taskEntity.setPriority(request.priority() == null ? TaskPriority.NOT_SELECTED : request.priority());
        taskEntity.setDescription(request.description() == null ? "No description" : request.description());
        taskEntity.setStatus(TaskStatus.IN_PROCESS);
        taskEntity.setProject(project);

        TaskEntity saved = taskRepository.save(taskEntity);
        return taskMapper.toResponse(saved);
    }


    @Transactional(readOnly = true)
    public TaskResponse getTask(Long currentUserId, Long taskId
    ){
        log.info("From SERVICE called getTask");
        
        //exist
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
            () -> new ResourceNotFoundException("Task doesn't exist with id = " + taskId));
        
        //member
        if (!projectRepository.existsByIdAndMemberId(task.getProject().getId(), currentUserId)){
            throw new AccessDeniedException("You are not member of this project with id = " + task.getProject().getId());  
        }

        return taskMapper.toResponse(task);

    } 

    @Transactional(readOnly = true)
    public TaskResponse getTaskFromProject(Long currentUserId, Long projectId, Long taskId
    ){
        log.info("From SERVICE called getTaskFromProject");

        //member
        if (!projectRepository.existsById(projectId)){
            throw new ResourceNotFoundException("Project doesn't exist with id = " + projectId);  
        }
        if (!projectRepository.existsByIdAndMemberId(projectId, currentUserId)){
            throw new AccessDeniedException("You are not member of this project with id = " + projectId);  
        }


        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
            () -> new ResourceNotFoundException("Task doesn't exist with id = " + taskId)
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
            throw new ResourceNotFoundException("Project doesn't exist with id = " + projectId);  
        }
        if (!projectRepository.existsByIdAndMemberId(projectId, currentUserId)){
            throw new AccessDeniedException("You are not member of this project with id = " + projectId);  
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

    @Transactional
    public void addExecutorToTask(Long currentUserId, Long taskId, Long userId
    ){
        log.info("From SERVICE called addExecutorToTask");

        //exist
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
            () -> new ResourceNotFoundException("Task doesn't exist with id = " + taskId));
        //owner
        if(!task.getProject().getOwner().getId().equals(currentUserId)){
            throw new AccessDeniedException("You are not owner of this project");
        }
        //exist
        if(!userRepository.existsById(userId)){
            throw new ResourceNotFoundException("User doesn't exist with id = " + userId);
        }
        //member
        if(!projectRepository.existsByIdAndMemberId(task.getProject().getId(), userId)){
            throw new IllegalArgumentException("User is not member of this project");
        }
        //duplicate
        if (taskRepository.existsByIdAndExecutorId(taskId, userId)){
            throw new DuplicateResourceException("User is already executor");
        }

        taskRepository.addExecutor(taskId, userId);
    }

    @Transactional
    public void addStatus(Long currentUserId, Long taskId, TaskStatusPatch request
    ){
        log.info("From SERVICE called addStatus");

        //exist
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
            () -> new ResourceNotFoundException("Task doesn't exist with id = " + taskId));
        //member
        if(!projectRepository.existsByIdAndMemberId(task.getProject().getId(), currentUserId)){
            throw new IllegalArgumentException("User is not member of this project");
        }

        if(projectRepository.existsByIdAndOwnerId(task.getProject().getId(), currentUserId)){
            task.setStatus(request.status());
        }else if(request.status().equals(TaskStatus.ON_REVIEW)){
            task.setStatus(request.status());
        }else{
            throw new AccessDeniedException("You can only change to ON_REVIEW");
        }
        
    }

    @Transactional
    public TaskResponse updateTask(Long currentUserId, Long taskId, TaskPatch request
    ){
        log.info("From SERVICE called updateTask");

        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
            () -> new ResourceNotFoundException("Task doesn't exist with id = " + taskId));

        if(!task.getProject().getOwner().getId().equals(currentUserId)){
            throw new AccessDeniedException("You are not owner of this project");
        }

        if(request.name() != null){
            if(request.name().isBlank()){
                throw new IllegalArgumentException("Name cant be blank");
            }
            task.setName(request.name());
        }
        if(request.description() != null){
            task.setDescription(request.description().isEmpty() ? null : request.description());
        }
        if(request.deadline() != null){
            task.setDeadline(request.deadline());
        }
        if(request.priority() != null){
            task.setPriority(request.priority());
        }

        
        return taskMapper.toResponse(task); 

    }


    @Transactional
    public void deleteTask(Long currentUserId, Long taskId
    ){
        log.info("From SERVICE called deleteTask");

        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id = " + taskId));

        if (!task.getProject().getOwner().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Only the project owner can delete tasks");
        }

        taskRepository.delete(task);
    }

    @Transactional
    public void deleteExecutor(Long currentUserId, Long taskId, Long userId
    ){
        log.info("From SERVICE called deleteExecutor");

        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id = " + taskId));

        if (!task.getProject().getOwner().getId().equals(currentUserId)) {
            throw new AccessDeniedException("Only the project owner can remove executors");
        }

        if (!taskRepository.existsByIdAndExecutorId(taskId, userId)) {
            throw new ResourceNotFoundException("User is not an executor of this task");
        }

        taskRepository.deleteExecutor(taskId, userId);
    }
}   
