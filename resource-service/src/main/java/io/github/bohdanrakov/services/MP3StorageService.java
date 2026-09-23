package io.github.bohdanrakov.services;

import io.github.bohdanrakov.dtos.MP3FileDTO;
import io.github.bohdanrakov.dtos.SongServiceStoreRequest;
import io.github.bohdanrakov.exceptions.IdListTooLargeException;
import io.github.bohdanrakov.exceptions.InvalidIdException;
import io.github.bohdanrakov.exceptions.MP3FileNotFoundException;
import io.github.bohdanrakov.exceptions.MP3FileUnparsableException;
import io.github.bohdanrakov.mappers.MP3FileMapper;
import io.github.bohdanrakov.mappers.TikaMetadataSongRequestMapper;
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
import org.springframework.transaction.annotation.Transactional;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MP3StorageService {

    private static final Logger logger = LoggerFactory.getLogger(MP3StorageService.class);

    private final MP3FileRepository mp3FileRepository;
    private final MP3FileMapper mp3FileMapper;
    private final TikaMetadataSongRequestMapper tikaMetadataSongRequestMapper;
    private final SongServiceCallerService songServiceCallerService;

    public MP3StorageService(MP3FileRepository mp3FileRepository, MP3FileMapper mp3FileMapper,
                             TikaMetadataSongRequestMapper tikaMetadataSongRequestMapper,
                             SongServiceCallerService songServiceCallerService) {
        this.mp3FileRepository = mp3FileRepository;
        this.mp3FileMapper = mp3FileMapper;
        this.tikaMetadataSongRequestMapper = tikaMetadataSongRequestMapper;
        this.songServiceCallerService = songServiceCallerService;
    }

    public Long storeMP3File(byte[] mp3content) {
        Metadata metadata = extractMetadata(mp3content);

        MP3File mp3File = new MP3File();
        mp3File.setByteContent(mp3content);
        MP3File savedMP3File = mp3FileRepository.save(mp3File);

        SongServiceStoreRequest songRequest = tikaMetadataSongRequestMapper.toSongRequest(metadata,
                savedMP3File.getId());
        songServiceCallerService.uploadSongMetadata(songRequest);

        return savedMP3File.getId();
    }

    private static Metadata extractMetadata(byte[] mp3content) {
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
        } catch (IOException | SAXException | TikaException exception) {
            logger.error(exception.getMessage(), exception);
            throw new MP3FileUnparsableException("The request body is invalid MP3. Only MP3 files are allowed");
        }
        return metadata;
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

    @Transactional
    public List<Long> deleteMP3FilesById(String ids) {
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

        List<Long> idsForDeletion = mp3FileRepository.findExistingIds(parsedIds);
        mp3FileRepository.deleteAllById(idsForDeletion);
        return idsForDeletion;
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
