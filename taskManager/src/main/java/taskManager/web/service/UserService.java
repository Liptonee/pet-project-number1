package taskManager.web.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taskManager.database.entity.QUserEntity;
import taskManager.database.entity.TaskEntity;
import taskManager.database.entity.UserEntity;
import taskManager.database.repository.ProjectRepository;
import taskManager.database.repository.TaskRepository;
import taskManager.database.repository.UserRepository;
import taskManager.mapper.PageMapper;
import taskManager.mapper.UserMapper;
import taskManager.web.dto.request.PasswordPatch;
import taskManager.web.dto.request.User;
import taskManager.web.dto.request.UserPatch;
import taskManager.web.dto.response.PageResponse;
import taskManager.web.dto.response.UserResponse;
import taskManager.web.dto.response.UsernameResponse;
import taskManager.web.exception.DuplicateResourceException;
import taskManager.web.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProjectRepository projectRepository;
    private final PageMapper pageMapper;
    private final TaskRepository taskRepository;

    @Transactional
    public UserResponse createUser(User userRequest
    ){
        log.info("From SERVICE called createUser");

        UserEntity userEntity = userMapper.toEntity(userRequest);

        if (userRepository.existsByUsername(userEntity.getUsername())){
            throw new DuplicateResourceException("Username already exists");
        }
        if (userRepository.existsByEmail(userEntity.getEmail())){
            throw new DuplicateResourceException("Email already exists");
        }

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setPrivateProfile(userRequest.privateProfile() == null ? Boolean.FALSE : userRequest.privateProfile());
        
        UserEntity saved = userRepository.save(userEntity);

        return userMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    @Cacheable(value="user", key = "#userId")
    public UserResponse getProfile(Long userId
    ){
        log.info("From SERVICE called getProfile");
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User doesn't exist with id = " + userId));
            
        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long currentUserId, Long userId
    ){
        log.info("From SERVICE called getUser");
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User doesn't exist with id = " + userId));
        if (!currentUserId.equals(userId) && user.getPrivateProfile()){
            throw new AccessDeniedException("Profile is private");
        }

        return userMapper.toResponse(user);
    }

    @Transactional(readOnly=true)
    public PageResponse<UsernameResponse> getAllUsersFromProject(Long currentUserId,
            Long projectId,
            Long taskId,
            Pageable pageable
    ){
        log.info("From SERVICE called getAllUsersFromProject");

        //member
        if (!projectRepository.existsById(projectId)){
            throw new ResourceNotFoundException("Project doesn't exist with id = " + projectId);  
        }
        if (!projectRepository.existsByIdAndMemberId(projectId, currentUserId)){
            throw new AccessDeniedException("You are not member of this project with id = " + projectId);  
        }

        QUserEntity user = QUserEntity.userEntity;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(user.participatedProjectsList.any().id.eq(projectId));
        
        if(taskId != null) builder.and(user.tasksList.any().id.eq(taskId));

        Page<UserEntity> page = userRepository.findAll(builder, pageable);
        return pageMapper.toPageResponse(page,userMapper::toUsernameResponse);
    }

    @Transactional(readOnly=true)
    public PageResponse<UserResponse> getAllUsersFromTask(Long currentUserId, Long taskId, Pageable pageable
    ){
        log.info("From SERVICE called getAllUsersFromTask");

        TaskEntity task = taskRepository.findById(taskId).orElseThrow(
            () -> new ResourceNotFoundException("Task doesn't exist with id = " + taskId));

        //member
        if (!projectRepository.existsByIdAndMemberId(task.getProject().getId(), currentUserId)){
            throw new AccessDeniedException("You are not member of this project with id = " + task.getProject().getId());  
        }

        QUserEntity user = QUserEntity.userEntity;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(user.tasksList.any().id.eq(taskId));

        Page<UserEntity> page = userRepository.findAll(builder, pageable);
        return pageMapper.toPageResponse(page, userMapper::toResponse);
    }

    @Transactional
    @CachePut(value = "user", key = "#currentUserId")
    public UserResponse updateUser(Long currentUserId, UserPatch request
    ){
        log.info("From SERVICE called updateUser");

        UserEntity user = userRepository.findById(currentUserId).orElseThrow(
            () -> new ResourceNotFoundException("User doesn't exist with id = " + currentUserId));

        if(request.email() != null){
            if (userRepository.existsByEmail(request.email())){
                throw new DuplicateResourceException("This email already in use");
            }
            user.setEmail(request.email());
        }


        if(request.username() != null){
            if(request.username().isBlank()){
                throw new IllegalArgumentException("Username cant be blank");
            }
            if (userRepository.existsByUsername(request.username())){
                throw new DuplicateResourceException("This username already in use");
            }
            user.setUsername(request.username());
        }

        if(request.privateProfile() != null){
            user.setPrivateProfile(request.privateProfile());
        }

        return userMapper.toResponse(user);
    }

    @Transactional
    public void updatePassword(Long currentUserId, PasswordPatch request
    ){  
        log.info("From SERVICE called updatePassword");

        if(!request.newPassword().equals(request.repeatNewPassword())){
            throw new IllegalArgumentException("Passwords do not match");
        }
        UserEntity user = userRepository.findById(currentUserId).orElseThrow(
            () -> new ResourceNotFoundException("User doesn't exist with id = " + currentUserId));

        if(!passwordEncoder.matches(request.oldPassword(), user.getPassword())){
            throw new IllegalArgumentException("Old passwords is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
    }



}
