package taskManager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import taskManager.database.entity.UserEntity;
import taskManager.web.dto.request.User;
import taskManager.web.dto.response.UserResponse;


@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE) 
public interface UserMapper{
    
    UserEntity toEntity(User dto);

    
    UserResponse toResponse(UserEntity entity);
}
