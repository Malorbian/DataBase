package com.jpw.database.model;

import com.jpw.database.model.mediaTypes.MediaBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_rating_platform_media",
                columnNames = {"platform_id", "media_id"})
})
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Rating {

    @Id
    private UUID id;

    @Setter
    @Column(nullable = false)
    private UUID platformId;

    @Setter
    @Column(nullable = false)
    private UUID mediaId;

    @Setter
    private double rating;

    public Rating(UUID id, UUID platform_ID, UUID media, double rating) {
        this.id = id;
        this.platformId = platform_ID;
        this.mediaId = media;
        this.rating = rating;
    }

}
