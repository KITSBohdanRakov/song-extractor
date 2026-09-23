package io.github.bohdanrakov.exceptions;

import org.springframework.http.HttpStatus;

public class MP3MetadataNotFoundException extends APIException {
    public MP3MetadataNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
