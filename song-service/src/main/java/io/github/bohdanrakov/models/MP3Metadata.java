package io.github.bohdanrakov.models;

import jakarta.persistence.*;

@Entity
@Table(name = "mp3_metadata")
public class MP3Metadata {
    @Id
    private Long id;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String artist;

    @Column(length = 100)
    private String album;

    @Column
    private Short duration;

    @Column
    private Short year;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public Short getDuration() {
        return duration;
    }

    public void setDuration(Short duration) {
        this.duration = duration;
    }

    public Short getYear() {
        return year;
    }

    public void setYear(Short year) {
        this.year = year;
    }
}
