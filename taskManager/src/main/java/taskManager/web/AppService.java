package taskManager.web;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import taskManager.database.entities.CommentEntity;
import taskManager.database.entities.ProjectEntity;
import taskManager.database.entities.TaskEntity;
import taskManager.database.entities.TaskPriority;
import taskManager.database.entities.TaskStatus;
import taskManager.database.entities.UserEntity;
import taskManager.database.repositories.CommentRepository;
import taskManager.database.repositories.ProjectRepository;
import taskManager.database.repositories.TaskRepository;
import taskManager.database.repositories.UserRepository;
import taskManager.mapper.CommentMapper;
import taskManager.mapper.ProjectMapper;
import taskManager.mapper.TaskMapper;
import taskManager.mapper.UserMapper;
import taskManager.web.dto.Comment;
import taskManager.web.dto.CommentResponse;
import taskManager.web.dto.Project;
import taskManager.web.dto.ProjectResponse;
import taskManager.web.dto.Task;
import taskManager.web.dto.TaskResponse;
import taskManager.web.dto.User;
import taskManager.web.dto.UserResponse;


@Service
@RequiredArgsConstructor
public class AppService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;



    @Transactional
    public UserResponse createUser(User userRequest) {

        UserEntity userEntity = userMapper.toEntity(userRequest);

        if (userRepository.existsByUsername(userEntity.getUsername())){
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(userEntity.getEmail())){
            throw new IllegalArgumentException("Email already exists");
        }

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        
        UserEntity saved = userRepository.save(userEntity);

        return userMapper.toResponse(saved);
    }

    @Transactional
    public ProjectResponse createProject(Long userId, Project projectRequest){

        UserEntity user = userRepository.findById(userId).get();


        if (projectRepository.existsByOwnerIdAndName(userId,projectRequest.name())){
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


    
    
    @Transactional
    public TaskResponse createTask(Long userId, Long projectId, Task request) {
        if (!projectRepository.existsByOwnerIdAndId(userId, projectId)){
            throw new IllegalArgumentException("User is not owner of this project with id = " + projectId);
        }   
        ProjectEntity project = projectRepository.findById(projectId).get();

        TaskEntity taskEntity = taskMapper.toEntity(request);
        taskEntity.setPriority(request.priority() == null ? TaskPriority.NOT_SELECTED : request.priority());
        taskEntity.setDescription(request.description() == null ? "No description" : request.description());
        taskEntity.setStatus(TaskStatus.IN_PROCESS);
        taskEntity.setProject(project);

        TaskEntity saved = taskRepository.save(taskEntity);
        project.getTasksList().add(saved);

        return taskMapper.toResponse(saved);
    }


    @Transactional
    public CommentResponse createComment(Long userId, Long taskId, Comment request) {
        UserEntity user = userRepository.findById(userId).get();
        TaskEntity task = taskRepository.findById(taskId)
                                             .orElseThrow(
                                                () -> new IllegalArgumentException("Task is not exist with id = " + taskId));
        
        ProjectEntity project = projectRepository.findById(task.getProject().getId()).get();

        if (!project.getMembersList().contains(user)){
            throw new IllegalArgumentException("User is not member of project");
        }

        CommentEntity commentEntity = commentMapper.toEntity(request);

        commentEntity.setTask(task);
        commentEntity.setUser(user);

        CommentEntity saved = commentRepository.save(commentEntity);

        user.getComments().add(saved);
        task.getCommentsList().add(saved);

        return commentMapper.toResponse(saved);
    }




}

