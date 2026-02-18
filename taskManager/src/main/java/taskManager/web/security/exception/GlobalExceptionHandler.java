package taskManager.web.security.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    

    @ExceptionHandler(exception = {Exception.class})
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception e) {
        var errorDto = new ErrorResponseDto(
            "Internal server error",
            e.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDto);
    }

    
    @ExceptionHandler(exception = {IllegalArgumentException.class,
                                    MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponseDto> handleValidException(Exception e) {
        var errorDto = new ErrorResponseDto(
            "Bad request",
            e.getMessage(),
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDto);
    }

}
