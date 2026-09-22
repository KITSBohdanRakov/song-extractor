package io.github.bohdanrakov.exceptions;

public class MP3FileNotFoundException extends RuntimeException {

    private final long mp3FileId;

    public MP3FileNotFoundException(long mp3FileId) {
        super();
        this.mp3FileId = mp3FileId;
    }

    public long getMp3FileId() {
        return mp3FileId;
    }
}
