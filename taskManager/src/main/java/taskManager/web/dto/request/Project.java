package taskManager.web.dto.request;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;




public record Project(
    @NotBlank(message="Name cant be empty")
    @Size(max = 15, min = 2)
    String name,

    @Size(max = 1000, message="Максимальная длина описания - 1000 символов")
    String description    
) {
    
}
