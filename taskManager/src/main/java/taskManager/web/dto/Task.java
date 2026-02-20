package taskManager.web.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import taskManager.database.entities.TaskPriority;
import taskManager.database.entities.TaskStatus;


public record Task(

    @NotBlank(message="name cant be empty")
    @Size(max = 15, min = 2)
    String name,


    @Size(max = 1000)
    String description,

    TaskStatus status,

    @FutureOrPresent
    LocalDate deadline,
    
    TaskPriority priority
) {
    
}
