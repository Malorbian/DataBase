package com.jpw.database.model.mediaTypes;

import com.jpw.database.model.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "media",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_media_title_artist",
                        columnNames = {"title", "artist_id", "discipline"}
                )
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class MediaBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "media_id")
    private Long id;

    @Setter
    @Column(nullable = false)
    private String title;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false)
    private Artist artist;

    @JoinColumn(nullable = false)
    @Enumerated(EnumType.STRING)
    private Discipline discipline;

    @Setter
    @Enumerated(EnumType.STRING)
    private State state = State.UNKNOWN;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "media_genres",
            joinColumns = @JoinColumn(name = "media_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "media_tags",
            joinColumns = @JoinColumn(name = "media_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    @Setter
    @Column(name = "last_activity")
    private LocalDate lastActivity;

    @Setter
    private String link;


    public MediaBase(String title, Artist artist, Discipline discipline) {
        this.title = title;
        this.artist = artist;
        this.discipline = discipline;
    }


    // --- Set edit methods ---

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }

    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
    }

}
