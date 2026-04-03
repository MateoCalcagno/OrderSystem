package ordersystem.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleNotFound(ResourceNotFoundException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("status", 404);

        return error;
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleRuntime(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("status", 400);
        return error;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, Object> error = new HashMap<>();
        error.put("status", 400);

        Map<String, String> fields = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err ->
            fields.put(err.getField(), err.getDefaultMessage())
        );

        error.put("errors", fields);

        return error;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, Object> handleAccessDenied(AccessDeniedException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", ex.getMessage());
        error.put("status", 403);
        return error;
    }
}