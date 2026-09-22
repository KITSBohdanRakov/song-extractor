package io.github.bohdanrakov.services;

import io.github.bohdanrakov.dtos.MP3FileDTO;
import io.github.bohdanrakov.exceptions.InvalidIdException;
import io.github.bohdanrakov.exceptions.MP3FileNotFoundException;
import io.github.bohdanrakov.exceptions.MP3FileUnparsableException;
import io.github.bohdanrakov.mappers.MP3FileMapper;
import io.github.bohdanrakov.models.MP3File;
import io.github.bohdanrakov.repositories.MP3FileRepository;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Service
public class MP3StorageService {

    private final MP3FileRepository mp3FileRepository;
    private final MP3FileMapper mp3FileMapper;
    private static final Logger logger = LoggerFactory.getLogger(MP3StorageService.class);

    public MP3StorageService(MP3FileRepository mp3FileRepository, MP3FileMapper mp3FileMapper) {
        this.mp3FileRepository = mp3FileRepository;
        this.mp3FileMapper = mp3FileMapper;
    }

    public Long storeMP3File(byte[] mp3content) {
        String mimeType = new Tika().detect(mp3content);
        if (!"audio/mpeg".equals(mimeType)) {
            throw new MP3FileUnparsableException("The request body is invalid MP3. Only MP3 files are allowed");
        }
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();

        Mp3Parser Mp3Parser = new Mp3Parser();
        try (TikaInputStream inputStream = TikaInputStream.get(mp3content)) {
            Mp3Parser.parse(inputStream, handler, metadata, parseContext);
            String[] metadataNames = metadata.names();
            Arrays.stream(metadataNames)
                    .forEach(name -> System.out.println(metadata.get(name)));
        } catch (IOException | SAXException | TikaException exception) {
            logger.error(exception.getMessage(), exception);
            throw new MP3FileUnparsableException("The request body is invalid MP3. Only MP3 files are allowed");
        }

        MP3File mp3File = new MP3File();
        mp3File.setByteContent(mp3content);
        MP3File savedMP3File = mp3FileRepository.save(mp3File);

        return savedMP3File.getId();
    }

    public MP3FileDTO getMP3File(long id) {
        if (id <= 0) {
            throw new InvalidIdException("Invalid value '" + id + "' for ID. Must be a positive integer");
        }
        Optional<MP3File> mp3File = mp3FileRepository.findById(id);
        if (mp3File.isEmpty()) {
            throw new MP3FileNotFoundException("Resource with ID=" + id + " not found");
        }
        return mp3FileMapper.toDto(mp3File.get());
    }
}
