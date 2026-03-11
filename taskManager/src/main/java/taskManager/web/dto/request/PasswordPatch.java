package taskManager.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordPatch(
    @NotNull
    String oldPassword,
    
    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, max = 50, message = "Пароль должен быть от 8 до 50 символов")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()-[{}]:;',?/*~$^+=<>]).{8,50}$",
        message = "Пароль должен содержать минимум 8 символов, " +
                  "одну цифру, одну заглавную, одну строчную букву и один спецсимвол")
    String newPassword,

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 8, max = 50, message = "Пароль должен быть от 8 до 50 символов")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()-[{}]:;',?/*~$^+=<>]).{8,50}$",
        message = "Пароль должен содержать минимум 8 символов, " +
                  "одну цифру, одну заглавную, одну строчную букву и один спецсимвол")
    String repeatNewPassword
) {
    
}
