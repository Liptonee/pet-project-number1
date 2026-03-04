package taskManager.web.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import taskManager.database.entity.UserEntity;
import taskManager.database.repository.UserRepository;
import taskManager.mapper.UserMapper;
import taskManager.web.dto.User;
import taskManager.web.dto.UserResponse;

@Service
@RequiredArgsConstructor
public class UserService {


    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


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



    public UserResponse getUser(Long currentUserId, Long userId){
        UserEntity user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User doesn't exist with id = " + userId));
        if (!currentUserId.equals(userId) && user.getIsPrivateProfile()){
            throw new IllegalArgumentException("Profile is private");
        }

        return userMapper.toResponse(user);
    }

}
