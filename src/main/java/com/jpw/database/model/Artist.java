package com.jpw.database.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "artists",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_artist_name_discipline",
                        columnNames = {"name", "discipline"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Discipline discipline;

    public Artist(String name, Discipline discipline) {
        this.name = name;
        this.discipline = discipline;
    }

}
