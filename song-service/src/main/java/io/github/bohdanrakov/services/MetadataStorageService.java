package io.github.bohdanrakov.services;

import io.github.bohdanrakov.SongMetadataRepository;
import io.github.bohdanrakov.dtos.MP3MetadataResponse;
import io.github.bohdanrakov.dtos.MP3MetadataStoreRequest;
import io.github.bohdanrakov.exceptions.IdListTooLargeException;
import io.github.bohdanrakov.exceptions.InvalidIdException;
import io.github.bohdanrakov.exceptions.MP3MetadataNotFoundException;
import io.github.bohdanrakov.exceptions.MetadataAlreadyExistsException;
import io.github.bohdanrakov.mappers.MP3MetadataToResponseMapper;
import io.github.bohdanrakov.mappers.MP3RequestToMetadataMapper;
import io.github.bohdanrakov.models.MP3Metadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MetadataStorageService {

    private final SongMetadataRepository songMetadataRepository;
    private final MP3RequestToMetadataMapper mp3RequestToMetadataMapper;
    private final MP3MetadataToResponseMapper mp3MetadataToResponseMapper;

    public MetadataStorageService(SongMetadataRepository songMetadataRepository,
                                  MP3RequestToMetadataMapper mp3RequestToMetadataMapper,
                                  MP3MetadataToResponseMapper mp3MetadataToResponseMapper) {
        this.songMetadataRepository = songMetadataRepository;
        this.mp3RequestToMetadataMapper = mp3RequestToMetadataMapper;
        this.mp3MetadataToResponseMapper = mp3MetadataToResponseMapper;
    }

    @Transactional
    public Long storeMP3Metadata(MP3MetadataStoreRequest mp3MetadataStoreRequest) {

        MP3Metadata mp3Metadata = mp3RequestToMetadataMapper.toEntity(mp3MetadataStoreRequest);
        if (songMetadataRepository.existsById(mp3Metadata.getId())) {
            throw new MetadataAlreadyExistsException("Metadata for resource ID=" + mp3Metadata.getId()
                    + " already exists");
        }
        MP3Metadata savedMP3Metadata = songMetadataRepository.save(mp3Metadata);

        return savedMP3Metadata.getId();
    }

    public MP3MetadataResponse getMP3Metadata(Long id) {
        if (id <= 0) {
            throw new InvalidIdException("Invalid value '" + id + "' for ID. Must be a positive integer");
        }

        Optional<MP3Metadata> mp3Metadata = songMetadataRepository.findById(id);
        if (mp3Metadata.isEmpty()) {
            throw new MP3MetadataNotFoundException("Song metadata for ID=" + id + " not found");
        }

        return mp3MetadataToResponseMapper.toDto(mp3Metadata.get());
    }

    @Transactional
    public List<Long> deleteMP3MetadataByIds(String ids) {
        if (ids.length() > 200) {
            throw new IdListTooLargeException("CSV string is too long: received " + ids.length()
                    + " characters, maximum allowed is 200");
        }

        List<Long> parsedIds = new ArrayList<>();

        for (String id : ids.split(",")) {
            if (!isValidLong(id)) {
                throw new InvalidIdException("Invalid ID format: '" + id + "'. Only positive integers are allowed");
            }
            parsedIds.add(Long.parseLong(id));
        }

        List<Long> idsToDelete = songMetadataRepository.findExistingIds(parsedIds);
        songMetadataRepository.deleteAllById(idsToDelete);
        return idsToDelete;

    }

    private boolean isValidLong(String number) {
        for (int i = 0; i < number.length(); i++) {
            if(!Character.isDigit(number.charAt(i))) {
                return false;
            }
        }
        return true;
    }

}
