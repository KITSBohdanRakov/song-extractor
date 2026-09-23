package io.github.bohdanrakov;

import io.github.bohdanrakov.models.MP3Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongMetadataRepository extends JpaRepository<MP3Metadata, Integer> {
}
