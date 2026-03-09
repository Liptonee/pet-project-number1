package taskManager.web.service;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import taskManager.database.entity.UserEntity;
import taskManager.database.repository.UserRepository;
import taskManager.mapper.UserMapper;
import taskManager.web.dto.User;
import taskManager.web.dto.UserResponse;
import taskManager.web.exception.DuplicateResourceException;
import taskManager.web.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


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
        userEntity.setIsPrivateProfile(userRequest.isPrivateProfile() == null ? false : userRequest.isPrivateProfile());
        
        UserEntity saved = userRepository.save(userEntity);

        return userMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
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
        if (!currentUserId.equals(userId) && user.getIsPrivateProfile()){
            throw new AccessDeniedException("Profile is private");
        }

        return userMapper.toResponse(user);
    }

}
