package io.github.bohdanrakov.exceptions;

import org.springframework.http.HttpStatus;

public class MetadataAlreadyExistsException extends APIException {
    public MetadataAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
