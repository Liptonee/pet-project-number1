package taskManager.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import taskManager.database.entity.UserEntity;
import taskManager.web.dto.User;
import taskManager.web.dto.UserResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-04T19:38:51+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserEntity toEntity(User dto) {
        if ( dto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setEmail( dto.email() );
        userEntity.setIsPrivateProfile( dto.isPrivateProfile() );
        userEntity.setPassword( dto.password() );
        userEntity.setUsername( dto.username() );

        return userEntity;
    }

    @Override
    public UserResponse toResponse(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String email = null;
        String username = null;
        Boolean isPrivateProfile = null;

        id = entity.getId();
        email = entity.getEmail();
        username = entity.getUsername();
        isPrivateProfile = entity.getIsPrivateProfile();

        UserResponse userResponse = new UserResponse( id, email, username, isPrivateProfile );

        return userResponse;
    }
}
