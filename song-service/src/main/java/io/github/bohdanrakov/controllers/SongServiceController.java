package io.github.bohdanrakov.controllers;

import io.github.bohdanrakov.dtos.MP3MetadataStoreRequest;
import io.github.bohdanrakov.services.MetadataStorageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SongServiceController {

    private final MetadataStorageService metadataStorageService;

    public SongServiceController(MetadataStorageService metadataStorageService) {
        this.metadataStorageService = metadataStorageService;
    }

    @PostMapping("/songs")
    public ResponseEntity<Map<String, Integer>> storeMP3Metadata(
            @Valid @RequestBody MP3MetadataStoreRequest mp3MetadataStoreRequest) {

        Integer id = metadataStorageService.storeMP3Metadata(mp3MetadataStoreRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("id", id));
    }

}
