package io.github.bohdanrakov.mappers;

import io.github.bohdanrakov.dtos.MP3FileDTO;
import io.github.bohdanrakov.models.MP3File;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MP3FileMapper {

    MP3FileDTO toDto(MP3File mp3File);
}
