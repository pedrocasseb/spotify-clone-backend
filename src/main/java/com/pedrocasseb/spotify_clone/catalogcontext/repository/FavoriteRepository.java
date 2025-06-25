package com.pedrocasseb.spotify_clone.catalogcontext.repository;

import com.pedrocasseb.spotify_clone.catalogcontext.domain.Favorite;
import com.pedrocasseb.spotify_clone.catalogcontext.domain.FavoriteId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, FavoriteId> {
}
