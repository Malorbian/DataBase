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
                name = "uk_franchise_name",
                columnNames = "name")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Franchise {

    @Id
    private UUID id;

    @Setter
    private String name;

    public Franchise(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
