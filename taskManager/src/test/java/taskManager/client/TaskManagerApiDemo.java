package taskManager.client;  // выберите свой пакет

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class TaskManagerApiDemo {


    private static final String BASE_URL = "http://localhost:8080";
    private static final int TIMEOUT_SECONDS = 30;


    public abstract static class APICall<T> {

        public abstract Call<T> apiFun();

        public T call() throws ApiException {
            try {
                Response<T> response = apiFun().execute();
                if (!response.isSuccessful()) {
                    String errorBody = response.errorBody() != null ? response.errorBody().string() : "";
                    throw new ApiException(
                            "Ошибка HTTP " + response.code() + ": " + response.message() + "\n" + errorBody
                    );
                }
                return response.body();
            } catch (Exception e) {
                throw new ApiException("Ошибка выполнения запроса: " + e.getMessage(), e);
            }
        }
    }


    public static class ApiException extends RuntimeException {

        public ApiException(String message) {
            super(message);
        }

        public ApiException(String message, Throwable cause) {
            super(message, cause);
        }
    }


    public interface TaskManagerService {

        //Публичные 
        @POST("/registration")
        Call<UserResponse> register(@Body UserRegistrationRequest request);

        @POST("/auth/login")
        Call<JwtAuthResponse> login(@Body UserCredentialRequest credentials);

        //Защищённые
        @GET("/me")
        Call<UserResponse> getProfile(@Header("Authorization") String authHeader);

        @POST("/projects")
        Call<ProjectResponse> createProject(
                @Header("Authorization") String authHeader,
                @Body ProjectCreateRequest request
        );

        @GET("/projects")
        Call<PageResponse<ProjectResponse>> getProjects(
                @Header("Authorization") String authHeader,
                @Query("isOwner") Boolean isOwner,
                @Query("page") int page,
                @Query("size") int size
        );

        @POST("/projects/{projectId}/tasks")
        Call<TaskResponse> createTask(
                @Header("Authorization") String authHeader,
                @Path("projectId") long projectId,
                @Body TaskCreateRequest request
        );

        @GET("/projects/{projectId}/tasks")
        Call<PageResponse<TaskResponse>> getTasksFromProject(
                @Header("Authorization") String authHeader,
                @Path("projectId") long projectId,
                @Query("status") String status,
                @Query("page") int page,
                @Query("size") int size
        );

        @POST("/tasks/{taskId}/comments")
        Call<CommentResponse> createComment(
                @Header("Authorization") String authHeader,
                @Path("taskId") long taskId,
                @Body CommentCreateRequest request
        );

        @GET("/tasks/{taskId}/comments")
        Call<PageResponse<CommentResponse>> getCommentsFromTask(
                @Header("Authorization") String authHeader,
                @Path("taskId") long taskId,
                @Query("page") int page,
                @Query("size") int size
        );
    }

    //Упрощённые dto
    //requests
    public static class UserRegistrationRequest {

        private String email;
        private String password;
        private String username;
        private Boolean privateProfile;

        public UserRegistrationRequest(String email, String password, String username, Boolean privateProfile) {
            this.email = email;
            this.password = password;
            this.username = username;
            this.privateProfile = privateProfile;
        }
    }

    public static class UserCredentialRequest {

        private String email;
        private String password;

        public UserCredentialRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }

    public static class ProjectCreateRequest {

        private String name;
        private String description;

        public ProjectCreateRequest(String name, String description) {
            this.name = name;
            this.description = description;
        }
    }

    public static class TaskCreateRequest {

        private String name;
        private String description;
        private String deadline;
        private String priority;// "LOW", "MEDIUM", "HIGH", "CRUCIAL"

        public TaskCreateRequest(String name, String description, String deadline, String priority) {
            this.name = name;
            this.description = description;
            this.deadline = deadline;
            this.priority = priority;
        }
    }

    public static class CommentCreateRequest {

        private String message;

        public CommentCreateRequest(String message) {
            this.message = message;
        }
    }

    //response
    public static class JwtAuthResponse {

        private String token;
        private String refreshToken;

        public String getToken() {
            return token;
        }

        public String getRefreshToken() {
            return refreshToken;
        }
    }

    public static class UserResponse {

        private Long id;
        private String email;
        private String username;
        private Boolean privateProfile;


        public Long getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getUsername() {
            return username;
        }

        public Boolean getPrivateProfile() {
            return privateProfile;
        }
    }

    public static class ProjectResponse {

        private Long id;
        private String name;
        private String description;
        private Long owner_id;
        private String createdAt;

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Long getOwner_id() {
            return owner_id;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }

    public static class TaskResponse {

        private Long id;
        private String name;
        private String description;
        private String status;
        private String priority;
        private String deadline;
        private Long project_id;


        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getStatus() {
            return status;
        }

        public String getPriority() {
            return priority;
        }

        public String getDeadline() {
            return deadline;
        }

        public Long getProject_id() {
            return project_id;
        }
    }

    public static class CommentResponse {

        private Long id;
        private String message;
        private Long user_id;
        private Long task_id;


        public Long getId() {
            return id;
        }

        public String getMessage() {
            return message;
        }

        public Long getUser_id() {
            return user_id;
        }

        public Long getTask_id() {
            return task_id;
        }
    }

    public static class PageResponse<T> {

        private List<T> content;
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean last;

        
        public List<T> getContent() {
            return content;
        }

        public int getPage() {
            return page;
        }

        public int getSize() {
            return size;
        }

        public long getTotalElements() {
            return totalElements;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public boolean isLast() {
            return last;
        }
    }


    private static TaskManagerService createClient() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context)
                        -> LocalDateTime.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        return retrofit.create(TaskManagerService.class);
    }

    //Демонстрация
    public static void main(String[] args) {
        TaskManagerService api = createClient();

        try {
            // Регистрация нового пользователя
            System.out.println("=== Регистрация ===");
            UserRegistrationRequest regRequest = new UserRegistrationRequest(
                    "demo@example.com",
                    "StrongPass1!",
                    "demouser",
                    false
            );
            UserResponse registeredUser = new APICall<UserResponse>() {
                @Override
                public Call<UserResponse> apiFun() {
                    return api.register(regRequest);
                }
            }.call();
            System.out.println("Зарегистрирован пользователь: " + registeredUser.getUsername() + ", id=" + registeredUser.getId());

            // Логин (получение токена)
            System.out.println("\n=== Логин ===");
            UserCredentialRequest loginRequest = new UserCredentialRequest("demo@example.com", "StrongPass1!");
            JwtAuthResponse authResponse = new APICall<JwtAuthResponse>() {
                @Override
                public Call<JwtAuthResponse> apiFun() {
                    return api.login(loginRequest);
                }
            }.call();
            String token = authResponse.getToken();
            String authHeader = "Bearer " + token;
            System.out.println("Получен токен: " + token.substring(0, 20) + "...");

            // Получение своего профиля (проверка токена)
            System.out.println("\n=== Мой профиль ===");
            UserResponse myProfile = new APICall<UserResponse>() {
                @Override
                public Call<UserResponse> apiFun() {
                    return api.getProfile(authHeader);
                }
            }.call();
            System.out.println("Профиль: email=" + myProfile.getEmail() + ", username=" + myProfile.getUsername());

            // Создание проекта
            System.out.println("\n=== Создание проекта ===");
            ProjectCreateRequest projectRequest = new ProjectCreateRequest("Демо-проект", "Проект для демонстрации API");
            ProjectResponse createdProject = new APICall<ProjectResponse>() {
                @Override
                public Call<ProjectResponse> apiFun() {
                    return api.createProject(authHeader, projectRequest);
                }
            }.call();
            System.out.println("Создан проект: " + createdProject.getName() + ", id=" + createdProject.getId());

            // Получение списка проектов
            System.out.println("\n=== Список моих проектов ===");
            PageResponse<ProjectResponse> projectsPage = new APICall<PageResponse<ProjectResponse>>() {
                @Override
                public Call<PageResponse<ProjectResponse>> apiFun() {
                    return api.getProjects(authHeader, null, 0, 10);
                }
            }.call();
            System.out.println("Всего проектов: " + projectsPage.getTotalElements());
            for (ProjectResponse p : projectsPage.getContent()) {
                System.out.println(" - " + p.getName() + " (id=" + p.getId() + ")");
            }

            // Создание задачи в проекте
            System.out.println("\n=== Создание задачи ===");
            TaskCreateRequest taskRequest = new TaskCreateRequest(
                    "Первая задача",
                    "Описание задачи",
                    null, // без дедлайна
                    "HIGH"
            );
            TaskResponse createdTask = new APICall<TaskResponse>() {
                @Override
                public Call<TaskResponse> apiFun() {
                    return api.createTask(authHeader, createdProject.getId(), taskRequest);
                }
            }.call();
            System.out.println("Создана задача: " + createdTask.getName() + ", id=" + createdTask.getId());

            // Получение списка задач из проекта
            System.out.println("\n=== Задачи проекта ===");
            PageResponse<TaskResponse> tasksPage = new APICall<PageResponse<TaskResponse>>() {
                @Override
                public Call<PageResponse<TaskResponse>> apiFun() {
                    return api.getTasksFromProject(authHeader, createdProject.getId(), null, 0, 10);
                }
            }.call();
            System.out.println("Всего задач: " + tasksPage.getTotalElements());
            for (TaskResponse t : tasksPage.getContent()) {
                System.out.println(" - " + t.getName() + " (статус: " + t.getStatus() + ")");
            }

            // Добавление комментария к задаче
            System.out.println("\n=== Добавление комментария ===");
            CommentCreateRequest commentRequest = new CommentCreateRequest("Тестовый комментарий");
            CommentResponse createdComment = new APICall<CommentResponse>() {
                @Override
                public Call<CommentResponse> apiFun() {
                    return api.createComment(authHeader, createdTask.getId(), commentRequest);
                }
            }.call();
            System.out.println("Комментарий добавлен, id=" + createdComment.getId());

            // Получение комментариев задачи
            System.out.println("\n=== Комментарии к задаче ===");
            PageResponse<CommentResponse> commentsPage = new APICall<PageResponse<CommentResponse>>() {
                @Override
                public Call<PageResponse<CommentResponse>> apiFun() {
                    return api.getCommentsFromTask(authHeader, createdTask.getId(), 0, 10);
                }
            }.call();
            System.out.println("Всего комментариев: " + commentsPage.getTotalElements());
            for (CommentResponse c : commentsPage.getContent()) {
                System.out.println(" - " + c.getMessage() + " (автор: " + c.getUser_id() + ")");
            }

            System.out.println("\n=== Демонстрация завершена успешно ===");

        } catch (ApiException e) {
            System.err.println("Ошибка при выполнении запроса: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
