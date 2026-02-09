package com.jpw.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "platforms",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_platform_name_discipline",
                        columnNames = {"name", "discipline"}
                )
        }
)
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class RatingPlatform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Discipline discipline;

    public RatingPlatform(String name, Discipline discipline) {
        this.name = name;
        this.discipline = discipline;
    }

}
