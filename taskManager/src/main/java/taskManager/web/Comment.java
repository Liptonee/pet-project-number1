package taskManager.web;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;



public record Comment(
    @Null
    Long id,

    @NotNull
    Task task,

    @NotEmpty(message="message cant be empty")
    @Size(max=150)
    String message,

    @NotNull
    User user,
    
    @NotNull
    LocalDateTime sendTime
) {
    
}
