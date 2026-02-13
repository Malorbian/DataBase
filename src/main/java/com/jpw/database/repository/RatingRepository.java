package com.jpw.database.repository;

import com.jpw.database.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {

    // Cache: all ratings for a collection of media IDs
    List<Rating> findByMediaIdIn(Collection<UUID> mediaIds);

    // Detail:
    List<Rating> findByMediaId(UUID mediaId);

    Optional<Rating> findByPlatformIdAndMediaId(UUID platformId, UUID mediaId);

    long deleteByMediaId(UUID mediaId);
}

