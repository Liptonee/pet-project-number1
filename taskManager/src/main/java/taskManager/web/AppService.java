package taskManager.web;

import org.springframework.stereotype.Service;

import taskManager.database.repositories.ProjectRepository;
import taskManager.database.repositories.TaskRepository;
import taskManager.database.repositories.UserRepository;
import taskManager.mapper.CommentMapper;
import taskManager.mapper.ProjectMapper;
import taskManager.mapper.TaskMapper;
import taskManager.mapper.UserMapper;

@Service
public class AppService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserMapper userMapper;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;
    private final CommentMapper commentMapper;

    public AppService(
        UserMapper userMapper,
        ProjectMapper projectMapper,
        TaskMapper taskMapper,
        CommentMapper commentMapper,
        UserRepository userRepository,
        ProjectRepository projectRepository,
        TaskRepository taskRepository
    ){
        this.commentMapper = commentMapper;
        this.userMapper = userMapper;
        this.projectMapper = projectMapper;
        this.taskMapper = taskMapper;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }



    public User createUser(User user) {
        var createdEntity = userRepository.save(userMapper.toEntity(user));
        return userMapper.toDto(createdEntity);
    }
    
}
