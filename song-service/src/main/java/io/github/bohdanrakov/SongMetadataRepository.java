package io.github.bohdanrakov;

import io.github.bohdanrakov.models.MP3Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongMetadataRepository extends JpaRepository<MP3Metadata, Integer> {

    @Query("SELECT m.id FROM MP3Metadata m WHERE m.id IN :requestIds")
    List<Integer> findExistingIds(List<Long> requestIds);
}
