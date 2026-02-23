package taskManager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import taskManager.database.entities.ProjectEntity;
import taskManager.web.dto.Project;
import taskManager.web.dto.ProjectResponse;

@Mapper(componentModel = "spring",uses = {UserMapper.class})
public interface ProjectMapper {
    
    @Mapping(source="owner.id",target="owner_id")
    ProjectResponse toResponse(ProjectEntity entity);
    
    ProjectEntity toEntity(Project dto);




}
