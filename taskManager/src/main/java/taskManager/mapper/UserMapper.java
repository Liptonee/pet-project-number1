package taskManager.mapper;

import org.mapstruct.Mapper;

import taskManager.database.entities.UserEntity;
import taskManager.web.User;


@Mapper(componentModel = "spring") 
public interface UserMapper {
    
    User toDto(UserEntity entity);

    UserEntity toEntity(User dto);
    
}
