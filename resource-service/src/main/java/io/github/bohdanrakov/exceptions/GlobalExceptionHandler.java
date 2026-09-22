package io.github.bohdanrakov.exceptions;


import io.github.bohdanrakov.dtos.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> unsupportedMediaType(HttpServletRequest request) {
        String contentTypeHeader = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if (contentTypeHeader == null) {
            contentTypeHeader = "No content type";
        }
        return new ResponseEntity<>(new ErrorResponse("Invalid file format: " + contentTypeHeader
                + ". Only MP3 files are allowed", String.valueOf(HttpStatus.BAD_REQUEST.value())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> invalidIdType(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("Invalid value '" + ex.getValue() + "' for ID. Must be a positive integer",
                        Integer.toString(HttpStatus.BAD_REQUEST.value())),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<ErrorResponse> handleApiException(APIException ex) {
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(), Integer.toString(ex.getHttpStatus().value())),
                ex.getHttpStatus());
    }
}
