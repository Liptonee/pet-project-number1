package taskManager.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import taskManager.database.entities.ProjectEntity;
import taskManager.web.Project;

@Mapper(componentModel = "spring",uses = {UserMapper.class})
public interface ProjectMapper {
    
    @Mapping(source = "owner",target = "owner")
    Project toDto(ProjectEntity entity);
    
    @Mapping(source = "owner", target = "owner")
    ProjectEntity toEntity(Project dto);

}
