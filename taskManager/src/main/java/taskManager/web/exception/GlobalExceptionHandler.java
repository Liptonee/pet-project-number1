package taskManager.web.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //500
    @ExceptionHandler(exception = Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e,
                                                                HttpServletRequest request) {
        log.error("Internal server error",e);
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", request);
    }

    //403
    @ExceptionHandler(exception = AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(AccessDeniedException e,
                                                                HttpServletRequest request){
        log.error("Access denied {}", e.getMessage());
        return buildErrorResponse(HttpStatus.FORBIDDEN, e.getMessage(), request);
    }

    //404
    @ExceptionHandler(exception = ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(ResourceNotFoundException e,
                                                                HttpServletRequest request){
        log.error("Resource not found {}", e.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, e.getMessage(), request);
    }

    //400
    @ExceptionHandler(exception = {IllegalArgumentException.class,MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestException(Exception e,
                                                                HttpServletRequest request){
        log.error("Bad request {}", e.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage(), request);
    }

    //409
    @ExceptionHandler(exception = DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(DuplicateResourceException e,
                                                                HttpServletRequest request){

        log.error("Duplicate resource: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.CONFLICT, e.getMessage(), request);
    }

    //401
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException e,
                                                              HttpServletRequest request) {
        log.error("Authentication error: {}", e.getMessage());
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, e.getMessage(), request);
    }


    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status,
                                                             String message,
                                                             HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(response);
    }

}
