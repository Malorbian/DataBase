package com.jpw.database.model;

import com.jpw.database.model.enums.Discipline;
import com.jpw.database.model.enums.State;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_series_name_discipline",
                columnNames = {"name", "discipline"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Series {

    @Id
    private UUID id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Discipline discipline;

    @Setter
    private int seasonNumber;

    @Setter
    private int episodeCount;

    @Setter
    private int currentEpisode;

    @Setter
    private int currentSeason;

    @Setter
    @Enumerated(EnumType.STRING)
    private State state;

    public Series(UUID id, String name, Discipline discipline) {
        this.id = id;
        this.name = name;
        this.discipline = discipline;
    }
}
