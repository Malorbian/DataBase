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
                name = "uk_media_genre_media_genre",
                columnNames = {"media_id", "genre_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MediaGenre {

    @Id
    private UUID id;

    @Setter
    @Column(nullable = false)
    private UUID mediaId;
    @Setter
    @Column(nullable = false)
    private UUID genreId;

    public MediaGenre(UUID id, UUID mediaId, UUID genreId) {
        this.id = id;
        this.mediaId = mediaId;
        this.genreId = genreId;
    }
}
