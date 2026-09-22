package io.github.bohdanrakov.controllers;

import io.github.bohdanrakov.dtos.MP3FileDTO;
import io.github.bohdanrakov.dtos.MP3StoreRequest;
import io.github.bohdanrakov.dtos.MP3StoreResponse;
import io.github.bohdanrakov.services.MP3StorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
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

    @GetMapping("/resources/{id}")
    public ResponseEntity<byte[]> getResource(@PathVariable Long id) {
        MP3FileDTO mp3File = mp3StorageService.getMP3File(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .body(mp3File.byteContent());
    }
}
