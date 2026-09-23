package io.github.bohdanrakov.dtos;

public record SongServiceStoreRequest(Long id, String name, String artist, String album, String duration, String year) {
}
