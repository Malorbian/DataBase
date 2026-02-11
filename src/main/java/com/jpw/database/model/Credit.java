package com.jpw.database.model;

import com.jpw.database.model.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_credit_media_person_role",
                columnNames = {"media_id", "person_id", "role"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Credit {

    @Id
    private UUID id;

    @Setter
    @Column(nullable = false)
    private UUID mediaId;

    @Setter
    @Column(nullable = false)
    private UUID personId;

    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public Credit(UUID id, UUID mediaId, UUID personId, Role role) {
        this.id = id;
        this.mediaId = mediaId;
        this.personId = personId;
        this.role = role;
    }
}
