package taskManager.mapper;

import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import taskManager.database.entities.ProjectEntity;
import taskManager.database.entities.UserEntity;
import taskManager.web.dto.Project;
import taskManager.web.dto.ProjectResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-23T21:05:18+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.45.0.v20260128-0750, environment: Java 21.0.9 (Eclipse Adoptium)"
)
@Component
public class ProjectMapperImpl implements ProjectMapper {

    @Override
    public ProjectResponse toResponse(ProjectEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Long owner_id = null;
        Long id = null;
        String name = null;
        String description = null;
        LocalDateTime createdAt = null;

        owner_id = entityOwnerId( entity );
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        createdAt = entity.getCreatedAt();

        ProjectResponse projectResponse = new ProjectResponse( id, name, description, owner_id, createdAt );

        return projectResponse;
    }

    @Override
    public ProjectEntity toEntity(Project dto) {
        if ( dto == null ) {
            return null;
        }

        ProjectEntity projectEntity = new ProjectEntity();

        projectEntity.setName( dto.name() );
        projectEntity.setDescription( dto.description() );

        return projectEntity;
    }

    private Long entityOwnerId(ProjectEntity projectEntity) {
        UserEntity owner = projectEntity.getOwner();
        if ( owner == null ) {
            return null;
        }
        return owner.getId();
    }
}
