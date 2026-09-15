package io.github.bohdanrakov.controllers;

import io.github.bohdanrakov.dtos.MP3StoreRequest;
import io.github.bohdanrakov.dtos.MP3StoreResponse;
import io.github.bohdanrakov.services.MP3StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceServiceController {

    private final MP3StorageService mp3StorageService;

    public ResourceServiceController(MP3StorageService mp3StorageService) {
        this.mp3StorageService = mp3StorageService;
    }

    @PostMapping(value = "/resources", consumes = "audio/mpeg")
    public ResponseEntity<MP3StoreResponse> addResource(@RequestBody MP3StoreRequest mp3StoreRequest) {
        Long storedFileId = mp3StorageService.storeMP3File(mp3StoreRequest.content());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MP3StoreResponse(storedFileId));
    }
}
