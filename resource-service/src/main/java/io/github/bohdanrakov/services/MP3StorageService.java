package io.github.bohdanrakov.services;

import io.github.bohdanrakov.models.MP3File;
import io.github.bohdanrakov.repositories.MP3FileRepository;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Arrays;

@Service
public class MP3StorageService {

    private final MP3FileRepository mp3FileRepository;

    public MP3StorageService(MP3FileRepository mp3FileRepository) {
        this.mp3FileRepository = mp3FileRepository;
    }

    public Long storeMP3File(byte[] mp3content) {
        MP3File mp3File = new MP3File();
        mp3File.setByteContent(mp3content);
        MP3File savedMP3File = mp3FileRepository.save(mp3File);

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
            throw new RuntimeException(exception);
        }

        return savedMP3File.getId();
    }
}
