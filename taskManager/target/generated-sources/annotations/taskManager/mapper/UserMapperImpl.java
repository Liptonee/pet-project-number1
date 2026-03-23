package taskManager.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import taskManager.database.entity.UserEntity;
import taskManager.web.dto.request.User;
import taskManager.web.dto.response.UserResponse;
import taskManager.web.dto.response.UsernameResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-23T18:47:00+0700",
    comments = "version: 1.7.0.Beta1, compiler: Eclipse JDT (IDE) 3.45.0.v20260224-0835, environment: Java 21.0.10 (Eclipse Adoptium)"
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
        userEntity.setPassword( dto.password() );
        userEntity.setPrivateProfile( dto.privateProfile() );
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
        Boolean privateProfile = null;

        id = entity.getId();
        email = entity.getEmail();
        username = entity.getUsername();
        privateProfile = entity.getPrivateProfile();

        UserResponse userResponse = new UserResponse( id, email, username, privateProfile );

        return userResponse;
    }

    @Override
    public UsernameResponse toUsernameResponse(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        String username = null;

        username = entity.getUsername();

        UsernameResponse usernameResponse = new UsernameResponse( username );

        return usernameResponse;
    }
}
