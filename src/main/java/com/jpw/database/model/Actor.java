package com.jpw.database.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
        name = "actors",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String name;

    public Actor(String name) {
        this.name = name;
    }
}
