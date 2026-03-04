package taskManager.web.service;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import taskManager.database.entity.CommentEntity;
import taskManager.database.entity.TaskEntity;
import taskManager.database.entity.UserEntity;
import taskManager.database.repository.CommentRepository;
import taskManager.database.repository.ProjectRepository;
import taskManager.database.repository.TaskRepository;
import taskManager.database.repository.UserRepository;
import taskManager.mapper.CommentMapper;
import taskManager.web.dto.Comment;
import taskManager.web.dto.CommentResponse;

@Service
@RequiredArgsConstructor
public class CommentService {
    

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;

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
