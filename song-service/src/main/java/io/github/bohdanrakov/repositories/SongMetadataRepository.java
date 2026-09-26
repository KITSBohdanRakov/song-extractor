package io.github.bohdanrakov.repositories;

import io.github.bohdanrakov.models.MP3Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongMetadataRepository extends JpaRepository<MP3Metadata, Long> {

    @Query("SELECT m.id FROM MP3Metadata m WHERE m.id IN :requestIds")
    List<Long> findExistingIds(List<Long> requestIds);
}
