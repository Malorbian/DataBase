package com.jpw.database.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_media_tag_media_tag",
                columnNames = {"media_id", "tag_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MediaTag {

    @Id
    private UUID id;

    @Setter
    @Column(nullable = false)
    private UUID mediaId;
    @Setter
    @Column(nullable = false)
    private UUID tagId;

    public MediaTag(UUID id, UUID mediaId, UUID genreId) {
        this.id = id;
        this.mediaId = mediaId;
        this.tagId = genreId;
    }
}
