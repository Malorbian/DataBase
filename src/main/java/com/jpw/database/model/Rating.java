package com.jpw.database.model;

import com.jpw.database.model.mediaTypes.MediaBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "ratings",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_rating_media_platform",
                        columnNames = {"media_id", "platform_id"}
                )
        }
)
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "platform_id")
    private RatingPlatform platform;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "media_media_id")
    private MediaBase media;

    @Setter
    private double rating;

    public Rating(RatingPlatform platform, MediaBase media, double rating) {
        this.platform = platform;
        this.media = media;
        this.rating = rating;
    }

}
