package taskManager.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserPatch(
    @Email
    String email,

    Boolean privateProfile,

    @Size(max = 15, min = 2)
    String username
) {
    
}
