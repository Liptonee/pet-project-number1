package taskManager.mapper;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import taskManager.database.entities.ProjectEntity;
import taskManager.web.Project;
import taskManager.web.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-18T20:46:14+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ProjectMapperImpl implements ProjectMapper {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Project toDto(ProjectEntity entity) {
        if ( entity == null ) {
            return null;
        }

        User owner = null;
        Long id = null;
        String name = null;
        String description = null;
        LocalDateTime createdAt = null;

        owner = userMapper.toDto( entity.getOwner() );
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        createdAt = entity.getCreatedAt();

        Project project = new Project( id, name, description, owner, createdAt );

        return project;
    }

    @Override
    public ProjectEntity toEntity(Project dto) {
        if ( dto == null ) {
            return null;
        }

        ProjectEntity projectEntity = new ProjectEntity();

        projectEntity.setOwner( userMapper.toEntity( dto.owner() ) );
        projectEntity.setCreatedAt( dto.createdAt() );
        projectEntity.setDescription( dto.description() );
        projectEntity.setId( dto.id() );
        projectEntity.setName( dto.name() );

        return projectEntity;
    }
}
