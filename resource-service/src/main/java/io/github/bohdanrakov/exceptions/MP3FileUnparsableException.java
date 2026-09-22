package io.github.bohdanrakov.exceptions;

import org.springframework.http.HttpStatus;

public class MP3FileUnparsableException extends APIException {
    public MP3FileUnparsableException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
