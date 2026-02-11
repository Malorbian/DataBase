package com.jpw.database.model;

import com.jpw.database.model.enums.Discipline;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_person_name_discipline",
                columnNames = {"name", "discipline"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Person {

    @Id
    private UUID id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Discipline discipline;

    public Person(UUID id, String name, Discipline discipline) {
        this.id = id;
        this.name = name;
        this.discipline = discipline;
    }
}
