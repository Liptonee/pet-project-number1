package taskManager.web.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;



public record Comment(
    @NotEmpty(message="message cant be empty")
    @Size(max=150)
    String message
) {
    
}
