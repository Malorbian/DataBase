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
                name = "uk_organisation_name",
                columnNames = "name")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Organisation {

    @Id
    private UUID id;

    @Setter
    @Column(nullable = false)
    private String name;

    public Organisation(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
