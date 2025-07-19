package br.com.crm.beauty.web.exceptions;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ch.qos.logback.core.boolex.EvaluationException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetails> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, List<String>> errorMap = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorMap.computeIfAbsent(fieldError.getField(), key -> new ArrayList<>())
                    .add(fieldError.getDefaultMessage());
        }

        Map<String, String[]> errors = new HashMap<>();
        errorMap.forEach((field, messages) -> errors.put(field, messages.toArray(new String[0])));

        ProblemDetails problem = new ProblemDetails();
        problem.setType("https://tools.ietf.org/html/rfc7231#section-6.5.1");
        problem.setTitle("Validation errors");
        problem.setStatus(HttpStatus.BAD_REQUEST.value());
        problem.setDetail("One or more validation errors occurred.");
        problem.setInstance(request.getRequestURI());
        problem.setTraceId(UUID.randomUUID().toString());
        problem.setErrors(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problem);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ProblemDetails> handleRuntimeException(
            RuntimeException ex,
            HttpServletRequest request) {

        Map<String, String[]> errors = new HashMap<>();
        errors.put("problem", new String[] { ex.getLocalizedMessage() });

        ProblemDetails problem = new ProblemDetails();
        problem.setType("https://tools.ietf.org/html/rfc7231#section-6.5.1");
        problem.setTitle("Runtime error");
        problem.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        problem.setDetail("An unexpected error occurred");
        problem.setInstance(request.getRequestURI());
        problem.setTraceId(UUID.randomUUID().toString());
        problem.setErrors(errors);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problem);

    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetails> handleDataIntegrityViolation(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {

        Map<String, String[]> errors = new HashMap<>();
        errors.put("problem", new String[] { "This resource already exists" });

        ProblemDetails problem = new ProblemDetails();
        problem.setType("https://tools.ietf.org/html/rfc7231#section-6.5.8");
        problem.setTitle("Unique constraint violation");
        problem.setStatus(HttpStatus.CONFLICT.value());
        problem.setDetail("A unique constraint was violated.");
        problem.setInstance(request.getRequestURI());
        problem.setTraceId(UUID.randomUUID().toString());
        problem.setErrors(errors);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problem);
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetails> handleNotFoundException(
            NotFoundException ex,
            HttpServletRequest request) {

        Map<String, String[]> errors = new HashMap<>();
        errors.put("problem", new String[] { ex.getMessage() });

        ProblemDetails problem = new ProblemDetails();
        problem.setType("https://tools.ietf.org/html/rfc7231#section-6.5.4");
        problem.setTitle("Not found");
        problem.setStatus(HttpStatus.NOT_FOUND.value());
        problem.setDetail("The requested resource was not found.");
        problem.setInstance(request.getRequestURI());
        problem.setTraceId(UUID.randomUUID().toString());
        problem.setErrors(errors);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problem);
    }

}
