package taskManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TaskManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagerApplication.class, args);
	}
	//http://localhost:8080/swagger-ui.html  http://localhost:8080/v3/api-docs
}

// {
//     "email": "traawiole@example.com",
//     "password": "2qwertY!da"
// }