CREATE TABLE mp3_metadata(
    id BIGINT PRIMARY KEY,
    duration SMALLINT,
    year SMALLINT,
    album VARCHAR(100),
    artist VARCHAR(100),
    name VARCHAR(100)
);