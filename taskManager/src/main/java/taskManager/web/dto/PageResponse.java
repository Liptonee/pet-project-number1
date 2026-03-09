package taskManager.web.dto;

import java.util.List;

public record  PageResponse<T>(
    List<T> content,
    Integer page,
    Integer size,
    Long totalElements,
    Integer totalPages,
    Boolean last
){
    
}
