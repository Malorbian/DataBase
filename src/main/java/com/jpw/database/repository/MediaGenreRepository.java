package com.jpw.database.repository;

import com.jpw.database.model.MediaGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaGenreRepository extends JpaRepository<MediaGenre, UUID> {

    // Cache: all genres for a media at once
    List<MediaGenre> findByMediaIdIn(Collection<UUID> mediaIds);

    // Detail / Editor:
    List<MediaGenre> findByMediaId(UUID mediaId);

    Optional<MediaGenre> findByMediaIdAndGenreId(UUID mediaId, UUID genreId);

    long deleteByMediaId(UUID mediaId);
}

