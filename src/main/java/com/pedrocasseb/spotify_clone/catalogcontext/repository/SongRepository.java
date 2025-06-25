package com.pedrocasseb.spotify_clone.catalogcontext.repository;

import com.pedrocasseb.spotify_clone.catalogcontext.domain.Song;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
}
