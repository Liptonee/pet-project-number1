package taskManager.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record User(
    @NotBlank(message="email cant be empty")
    @Email
    String email,

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, max = 50, message = "Пароль должен быть от 8 до 50 символов")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()-[{}]:;',?/*~$^+=<>]).{8,50}$",
        message = "Пароль должен содержать минимум 8 символов, " +
                  "одну цифру, одну заглавную, одну строчную букву и один спецсимвол"
    )
    String password,

    Boolean privateProfile,

    @NotBlank(message="name cant be empty")
    @Size(max = 15, min = 2)
    String username
) {
    

}
