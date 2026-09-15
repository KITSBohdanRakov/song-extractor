package io.github.bohdanrakov.repositories;


import io.github.bohdanrakov.models.MP3File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MP3FileRepository extends JpaRepository<MP3File, Long> {
}
