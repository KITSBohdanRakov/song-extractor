package io.github.bohdanrakov.exceptions;


import io.github.bohdanrakov.dtos.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> unsupportedMediaType(HttpServletRequest request) {
        String contentTypeHeader = request.getHeader(HttpHeaders.CONTENT_TYPE);
        if (contentTypeHeader == null) {
            contentTypeHeader = "No content type";
        }
        return new ResponseEntity<>(new ErrorResponse("Invalid file format: " + contentTypeHeader
                + ". Only MP3 files are allowed"), HttpStatus.BAD_REQUEST);
    }
}
