package io.github.bohdanrakov.exceptions;

import org.springframework.http.HttpStatus;

public class MP3FileNotFoundException extends APIException {
    public MP3FileNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
