package taskManager.web.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taskManager.database.entity.CommentEntity;
import taskManager.database.entity.QCommentEntity;
import taskManager.database.entity.TaskEntity;
import taskManager.database.entity.UserEntity;
import taskManager.database.repository.CommentRepository;
import taskManager.database.repository.ProjectRepository;
import taskManager.database.repository.TaskRepository;
import taskManager.database.repository.UserRepository;
import taskManager.kafka.KafkaProducerService;
import taskManager.mapper.CommentMapper;
import taskManager.mapper.PageMapper;
import taskManager.web.dto.request.Comment;
import taskManager.web.dto.response.CommentResponse;
import taskManager.web.dto.response.PageResponse;
import taskManager.web.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final PageMapper pageMapper;
    private final KafkaProducerService kafkaProducerService;


    @Transactional
    public CommentResponse createComment(Long currentUserId, Long taskId, Comment request
    ){
        log.info("From SERVICE called createComment");

        UserEntity user = userRepository.findById(currentUserId).orElseThrow(
            () -> new ResourceNotFoundException("User doesn't exist with id = " + currentUserId)
        );
        TaskEntity task = taskRepository.findById(taskId)
                                             .orElseThrow(
                                                () -> new ResourceNotFoundException("Task doesn't exist with id = " + taskId));
        

        if (!projectRepository.existsByIdAndMemberId(task.getProject().getId(), currentUserId)){
            throw new AccessDeniedException("User doesn't member of project");
        }

        CommentEntity commentEntity = commentMapper.toEntity(request);

        commentEntity.setTask(task);
        commentEntity.setUser(user);

        CommentEntity saved = commentRepository.save(commentEntity);
        kafkaProducerService.sendCommentCreatedEvent(saved.getMessage(), task, user.getUsername(), currentUserId);

        return commentMapper.toResponse(saved);
    }


    @Transactional(readOnly = true)
    public CommentResponse getComment(Long currentUserId, Long commentId
    ){
        log.info("From SERVICE called getComment");
        
        //sender (owner)
        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(
            () -> new ResourceNotFoundException("Comment doesn't exist with id = " + commentId)
        );
        if (!comment.getUser().getId().equals(currentUserId)){
            throw new AccessDeniedException("You are not send this comment");
        }

        return commentMapper.toResponse(comment);
    } 

    @Transactional(readOnly = true)
    public CommentResponse getCommentFromTask(Long currentUserId, Long taskId, Long commentId
    ){
        log.info("From SERVICE called getCommentFromTask");

        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
            () -> new ResourceNotFoundException("Task doesn't exist with id = " + taskId)
        );

        //member
        if (!projectRepository.existsById(task.getProject().getId())){
            throw new ResourceNotFoundException("Project doesn't exist with id = " + task.getProject().getId());  
        }
        if (!projectRepository.existsByIdAndMemberId(task.getProject().getId(), currentUserId)){
            throw new AccessDeniedException("You are not member of this project with id = " + task.getProject().getId());  
        }

        CommentEntity comment = commentRepository.findById(commentId).orElseThrow(
            () -> new ResourceNotFoundException("Comment doesn't exist with id = " + commentId)
        );

        //comment in task
        if(!comment.getTask().getId().equals(taskId)){
            throw new IllegalArgumentException("Comment doesn't belong to this task");
        }

        return commentMapper.toResponse(comment);
    }

    @Transactional(readOnly = true)
    public PageResponse<CommentResponse> getAllComments(Long currentUserId, Long taskId, Pageable pageable
    ){
        log.info("From SERVICE called getAllComments");

        QCommentEntity comment = QCommentEntity.commentEntity;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(comment.user.id.eq(currentUserId));
        if (taskId != null) builder.and(comment.task.id.eq(taskId));

        Page<CommentEntity> page = commentRepository.findAll(builder, pageable);
        return pageMapper.toPageResponse(page, commentMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public PageResponse<CommentResponse> getAllCommentsFromTask(Long currentUserId, Long taskId, Long userId, Pageable pageable
    ){
        log.info("From SERVICE called getAllCommentsFromTask");

        //member
        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
            () -> new ResourceNotFoundException("Task doesn't exist with id = " + taskId)
        );
        if (!projectRepository.existsById(task.getProject().getId())){
            throw new ResourceNotFoundException("Project doesn't exist with id = " + task.getProject().getId());  
        }
        if (!projectRepository.existsByIdAndMemberId(task.getProject().getId(), currentUserId)){
            throw new AccessDeniedException("You are not member of this project with id = " + task.getProject().getId());  
        }
 
        QCommentEntity comment = QCommentEntity.commentEntity;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(comment.task.id.eq(taskId));
        if(userId != null) builder.and(comment.user.id.eq(userId));

        Page<CommentEntity> page = commentRepository.findAll(builder, pageable);
        return pageMapper.toPageResponse(page, commentMapper::toResponse);
    }


}
