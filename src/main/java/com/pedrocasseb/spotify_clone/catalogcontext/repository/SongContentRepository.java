package com.pedrocasseb.spotify_clone.catalogcontext.repository;

import com.pedrocasseb.spotify_clone.catalogcontext.domain.SongContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongContentRepository extends JpaRepository<SongContent, Long> {
}
