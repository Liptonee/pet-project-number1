package taskManager.web.dto;

public record UserResponse(
    Long id,
    String email,
    String username,
    Boolean isPrivateProfile
) {

}
