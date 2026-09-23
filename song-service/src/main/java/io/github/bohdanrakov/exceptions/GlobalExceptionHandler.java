package io.github.bohdanrakov.exceptions;

import io.github.bohdanrakov.dtos.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleApiException(MethodArgumentNotValidException ex) {
        Map<String, String> errorDetails = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorDetails.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(
                new ValidationErrorResponse("Validation error", errorDetails,
                        Integer.toString(HttpStatus.BAD_REQUEST.value())),
                HttpStatus.BAD_REQUEST);
    }
}
