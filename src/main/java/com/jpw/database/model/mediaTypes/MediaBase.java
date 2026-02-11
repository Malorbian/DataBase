package com.jpw.database.model.mediaTypes;

import com.jpw.database.model.enums.Discipline;
import com.jpw.database.model.enums.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(
        name = "media",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "uk_media_title_org_discipline",
                    columnNames = {"title", "organisation_id", "discipline"}),
            @UniqueConstraint(
                    name = "uk_media_title_person_discipline",
                    columnNames = {"title", "person_id", "discipline"})
        }
)
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public abstract class MediaBase {

    @Id
    @Column(name = "media_id")
    private UUID id;

    @Setter
    @Column(nullable = false)
    private String title;

    @Setter
    @Column(name = "organisation_id")
    private UUID primaryOrganisationId;

    @Setter
    @Column(name = "person_id")
    private UUID primaryPersonId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Discipline discipline;

    @Setter
    @Enumerated(EnumType.STRING)
    private State state;

    @Setter
    private LocalDate lastActivity;

    @Setter
    private String link;


    public MediaBase(UUID id, String title, Discipline discipline) {
        this.id = id;
        this.title = title;
        this.discipline = discipline;
    }

}
