package taskManager.mapper;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import taskManager.Project;
import taskManager.User;
import taskManager.database.entities.ProjectEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-16T20:04:26+0700",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
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
        projectEntity.setId( dto.id() );
        projectEntity.setName( dto.name() );
        projectEntity.setDescription( dto.description() );
        projectEntity.setCreatedAt( dto.createdAt() );

        return projectEntity;
    }
}
