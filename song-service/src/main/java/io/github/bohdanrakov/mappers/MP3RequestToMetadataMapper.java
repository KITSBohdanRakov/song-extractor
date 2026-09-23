package io.github.bohdanrakov.mappers;

import io.github.bohdanrakov.dtos.MP3MetadataStoreRequest;
import io.github.bohdanrakov.models.MP3Metadata;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MP3RequestToMetadataMapper {

    @Mapping(target = "duration", source = "duration", qualifiedByName = "durationToShort")
    @Mapping(target = "year", source = "year", qualifiedByName = "yearToShort")
    MP3Metadata toEntity(MP3MetadataStoreRequest mp3MetadataStoreRequest);

    @Named("durationToShort")
    default Short durationStringToShort(String duration) {
        String[] minutesAndSeconds = duration.split(":");
        int minutes = Integer.parseInt(minutesAndSeconds[0]);
        int seconds = Integer.parseInt(minutesAndSeconds[1]);
        return (short) (minutes * 60 + seconds);
    }

    @Named("yearToShort")
    default Short yearStringToShort(String year) {
        return Short.parseShort(year);
    }
}
