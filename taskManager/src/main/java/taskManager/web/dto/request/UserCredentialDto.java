package taskManager.web.dto.request;

import lombok.Data;

@Data
public class UserCredentialDto {
    private String email;
    private String password;
}
