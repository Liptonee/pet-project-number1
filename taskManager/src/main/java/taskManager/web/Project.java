package taskManager.web;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;




public record Project(
    @Null
    Long id,

    @NotBlank(message="name cant be empty")
    @Size(max = 15, min = 2)
    String name,

    @Size(max = 1000)
    String description,
    
    @NotNull
    User owner,
    
    LocalDateTime createdAt
) {
    
}
