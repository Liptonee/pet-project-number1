package taskManager.web.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import taskManager.database.entity.ProjectEntity;
import taskManager.database.entity.TaskEntity;
import taskManager.database.entity.TaskPriority;
import taskManager.database.entity.TaskStatus;
import taskManager.database.repository.ProjectRepository;
import taskManager.database.repository.TaskRepository;
import taskManager.mapper.TaskMapper;
import taskManager.web.dto.Task;
import taskManager.web.dto.TaskResponse;

@RequiredArgsConstructor
@Service
public class TaskService {
    


    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectRepository projectRepository;

    @Transactional
    public TaskResponse createTask(Long currentUserId, Long projectId, Task request) {
        if (!projectRepository.existsByOwnerIdAndId(currentUserId, projectId)){
            throw new IllegalArgumentException("User doesn't owner of this project with id = " + projectId);
        }   
        ProjectEntity project = projectRepository.findById(projectId).orElseThrow(
            () -> new IllegalArgumentException("Project doesn't exist with id = " + projectId)
        );

        TaskEntity taskEntity = taskMapper.toEntity(request);
        taskEntity.setPriority(request.priority() == null ? TaskPriority.NOT_SELECTED : request.priority());
        taskEntity.setDescription(request.description() == null ? "No description" : request.description());
        taskEntity.setStatus(TaskStatus.IN_PROCESS);
        taskEntity.setProject(project);

        TaskEntity saved = taskRepository.save(taskEntity);
        project.getTasksList().add(saved);

        return taskMapper.toResponse(saved);
    }

    public TaskResponse getTask(Long currentUserId, Long taskId){
        
        //exist
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
            () -> new IllegalArgumentException("Task doesn't exist with id = " + taskId)
        );
        
        //member
        if(!projectRepository.existsByProjectIdAndMemberId(task.getProject().getId(), currentUserId)){
            throw new IllegalArgumentException("You are not member of this project or project doesn't exist");
        }

        return taskMapper.toResponse(task);

    } 


    public TaskResponse getTaskFromProject(Long currentUserId, Long projectId, Long taskId){

        //member
        if (!projectRepository.existsByProjectIdAndMemberId(projectId, currentUserId)){
            throw new IllegalArgumentException("You are not member of this project or project doesn't exist");
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

}
