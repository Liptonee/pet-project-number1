package taskManager.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    

    @ExceptionHandler(exception = {Exception.class})
    public ResponseEntity<String> handleGenericException(Exception e) {
        return ResponseEntity.status(413).body(e.getMessage());
    }

    
}
