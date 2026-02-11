package com.jpw.database.model.mediaTypes;

import com.jpw.database.model.enums.Discipline;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@PrimaryKeyJoinColumn(name = "media_id")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Music extends MediaBase {

    @Setter
    private String album;

    public Music(UUID id, String title) {
        super(id, title, Discipline.MUSIC);
    }

}
