package com.jpw.database.model.mediaTypes;

import com.jpw.database.model.enums.Discipline;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@PrimaryKeyJoinColumn(name = "media_id")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Game extends MediaBase {

    @Setter
    private String version;

    public Game(UUID id, String title) {
        super(id, title, Discipline.GAME);
    }

}
