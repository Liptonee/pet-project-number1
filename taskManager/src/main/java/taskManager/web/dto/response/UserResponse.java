package taskManager.web.dto.response;

public record UserResponse(
    Long id,
    String email,
    String username,
    Boolean privateProfile
) {

}
