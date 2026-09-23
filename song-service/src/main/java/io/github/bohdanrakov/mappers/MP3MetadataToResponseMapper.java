package io.github.bohdanrakov.mappers;


import io.github.bohdanrakov.dtos.MP3MetadataResponse;
import io.github.bohdanrakov.models.MP3Metadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MP3MetadataToResponseMapper {

    @Mapping(target = "duration", source = "duration", qualifiedByName = "shortToDuration")
    MP3MetadataResponse toDto(MP3Metadata mp3MetadataResponse);

    @Named("shortToDuration")
    default String shortToDurationString(Short duration) {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
