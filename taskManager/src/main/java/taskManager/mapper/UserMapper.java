package taskManager.mapper;

import org.mapstruct.Mapper;

import taskManager.User;
import taskManager.database.entities.UserEntity;


@Mapper(componentModel = "spring") 
public interface UserMapper {
    
    User toDto(UserEntity entity);

    UserEntity toEntity(User dto);
    
}
