package io.github.bohdanrakov.services;

import io.github.bohdanrakov.SongMetadataRepository;
import io.github.bohdanrakov.dtos.MP3MetadataStoreRequest;
import io.github.bohdanrakov.exceptions.MetadataAlreadyExistsException;
import io.github.bohdanrakov.mappers.MP3RequestToMetadataMapper;
import io.github.bohdanrakov.models.MP3Metadata;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MetadataStorageService {

    private final SongMetadataRepository songMetadataRepository;
    private final MP3RequestToMetadataMapper mp3RequestToMetadataMapper;

    public MetadataStorageService(SongMetadataRepository songMetadataRepository,
                                  MP3RequestToMetadataMapper mp3RequestToMetadataMapper) {
        this.songMetadataRepository = songMetadataRepository;
        this.mp3RequestToMetadataMapper = mp3RequestToMetadataMapper;
    }

    @Transactional
    public Integer storeMP3Metadata(MP3MetadataStoreRequest mp3MetadataStoreRequest) {

        MP3Metadata mp3Metadata = mp3RequestToMetadataMapper.toEntity(mp3MetadataStoreRequest);
        if (songMetadataRepository.existsById(mp3Metadata.getId())) {
            throw new MetadataAlreadyExistsException("Metadata for resource ID=" + mp3Metadata.getId()
                    + " already exists");
        }
        MP3Metadata savedMP3Metadata = songMetadataRepository.save(mp3Metadata);

        return savedMP3Metadata.getId();
    }

}
