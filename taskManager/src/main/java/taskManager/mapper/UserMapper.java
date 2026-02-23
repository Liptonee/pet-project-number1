package taskManager.mapper;

import org.mapstruct.Mapper;

import taskManager.database.entities.UserEntity;
import taskManager.web.dto.User;
import taskManager.web.dto.UserResponse;


@Mapper(componentModel = "spring") 
public interface UserMapper{
    
    UserEntity toEntity(User dto);
    UserResponse toResponse(UserEntity entity);
}
