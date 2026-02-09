package com.jpw.database.model.mediaTypes;

import com.jpw.database.model.Artist;
import com.jpw.database.model.Discipline;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "games")
@PrimaryKeyJoinColumn(name = "media_id")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Game extends MediaBase {

    @Setter
    private String version;

    public Game(String title, Artist artist) {
        super(title, artist, Discipline.GAMES);
    }

}
