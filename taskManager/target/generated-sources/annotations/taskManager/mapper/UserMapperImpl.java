package taskManager.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import taskManager.database.entities.UserEntity;
import taskManager.web.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-18T20:46:14+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toDto(UserEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String email = null;
        String password = null;
        String username = null;

        id = entity.getId();
        email = entity.getEmail();
        password = entity.getPassword();
        username = entity.getUsername();

        User user = new User( id, email, password, username );

        return user;
    }

    @Override
    public UserEntity toEntity(User dto) {
        if ( dto == null ) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setEmail( dto.email() );
        userEntity.setId( dto.id() );
        userEntity.setPassword( dto.password() );
        userEntity.setUsername( dto.username() );

        return userEntity;
    }
}
