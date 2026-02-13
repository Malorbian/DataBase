package com.jpw.database.repository;

import com.jpw.database.model.MediaTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MediaTagRepository extends JpaRepository<MediaTag, UUID> {

    // Cache: all tags for a media at once
    List<MediaTag> findByMediaIdIn(Collection<UUID> mediaIds);

    // Detail / Editor:
    List<MediaTag> findByMediaId(UUID mediaId);

    Optional<MediaTag> findByMediaIdAndTagId(UUID mediaId, UUID tagId);

    long deleteByMediaId(UUID mediaId);
}

