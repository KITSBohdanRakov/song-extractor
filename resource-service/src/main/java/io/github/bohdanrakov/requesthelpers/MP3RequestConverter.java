package io.github.bohdanrakov.requesthelpers;

import io.github.bohdanrakov.dtos.MP3StoreRequest;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;

import java.io.IOException;

public class MP3RequestConverter extends AbstractHttpMessageConverter<MP3StoreRequest> {

    public MP3RequestConverter() {
        super(MediaType.valueOf("audio/mpeg"));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return MP3StoreRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType) {
        return false;
    }

    @Override
    protected MP3StoreRequest readInternal(Class<? extends MP3StoreRequest> clazz, HttpInputMessage inputMessage) throws IOException {
        byte[] mp3ByteContent = inputMessage.getBody().readAllBytes();
        return new MP3StoreRequest(mp3ByteContent);
    }

    @Override
    protected void writeInternal(MP3StoreRequest mp3StoreRequest, HttpOutputMessage outputMessage) {
    }
}
