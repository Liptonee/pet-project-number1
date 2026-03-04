package taskManager.web.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import taskManager.database.entity.CommentEntity;
import taskManager.database.entity.ProjectEntity;
import taskManager.database.entity.TaskEntity;
import taskManager.database.entity.TaskPriority;
import taskManager.database.entity.TaskStatus;
import taskManager.database.entity.UserEntity;
import taskManager.database.repository.CommentRepository;
import taskManager.database.repository.ProjectRepository;
import taskManager.database.repository.TaskRepository;
import taskManager.database.repository.UserRepository;
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
        userEntity.setIsPrivateProfile(userRequest.isPrivateProfile() == null ? false : userRequest.isPrivateProfile());
        
        UserEntity saved = userRepository.save(userEntity);

        return userMapper.toResponse(saved);
    }

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


    @Transactional
    public CommentResponse createComment(Long currentUserId, Long taskId, Comment request) {
        UserEntity user = userRepository.findById(currentUserId).orElseThrow(
            () -> new IllegalArgumentException("User doesn't exist with id = " + currentUserId)
        );
        TaskEntity task = taskRepository.findById(taskId)
                                             .orElseThrow(
                                                () -> new IllegalArgumentException("Task doesn't exist with id = " + taskId));
        

        if (!projectRepository.existsByProjectIdAndMemberId(task.getProject().getId(), currentUserId)){
            throw new IllegalArgumentException("User doesn't member of project");
        }

        CommentEntity commentEntity = commentMapper.toEntity(request);

        commentEntity.setTask(task);
        commentEntity.setUser(user);

        CommentEntity saved = commentRepository.save(commentEntity);

        user.getComments().add(saved);
        task.getCommentsList().add(saved);

        return commentMapper.toResponse(saved);
    }




    public UserResponse getUser(Long currentUserId, Long userId){
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User doesn't exist with id = " + userId));
        if (!currentUserId.equals(userId) && user.getIsPrivateProfile()){
            throw new IllegalArgumentException("Profile is private");
        }

        return userMapper.toResponse(user);
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

    public CommentResponse getComment(Long currentUserId, Long commentId){
        
        //sender (owner)
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(
            () -> new IllegalArgumentException("Comment doesn't exist with id = " + commentId)
        );
        if (!comment.getUser().getId().equals(currentUserId)){
            throw new IllegalArgumentException("You are not send this comment");
        }

        return commentMapper.toResponse(comment);
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


    public CommentResponse getCommentFromTask(Long currentUserId, Long taskId, Long commentId){
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
            () -> new IllegalArgumentException("Task doesn't exist with id = " + taskId)
        );

        //member
        if (!projectRepository.existsByProjectIdAndMemberId(task.getProject().getId(), currentUserId)){
            throw new IllegalArgumentException("You are not member of this project or project doesn't exist");
        }

        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(
            () -> new IllegalArgumentException("Comment doesn't exist with id = " + taskId)
        );

        //comment in task
        if(!comment.getTask().getId().equals(taskId)){
            throw new IllegalArgumentException("Comment doesn't belong to this task");
        }

        return commentMapper.toResponse(comment);
    }

}

