package io.github.bohdanrakov.controllers;

import io.github.bohdanrakov.dtos.MP3MetadataResponse;
import io.github.bohdanrakov.dtos.MP3MetadataStoreRequest;
import io.github.bohdanrakov.services.MetadataStorageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/songs/{id}")
    public ResponseEntity<MP3MetadataResponse> getMP3Metadata(@PathVariable Integer id) {

        MP3MetadataResponse mp3Metadata = metadataStorageService.getMP3Metadata(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(mp3Metadata);
    }

    @DeleteMapping("/songs")
    public ResponseEntity<Map<String, List<Integer>>> deleteMP3Metadata(@RequestParam("id") String ids) {
        List<Integer> deletedIds = metadataStorageService.deleteMP3MetadataByIds(ids);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Map.of("ids", deletedIds));
    }

}
