package com.jpw.database.model.mediaTypes;

import com.jpw.database.model.Artist;
import com.jpw.database.model.Discipline;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stories")
@PrimaryKeyJoinColumn(name = "media_id")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Story extends MediaBase {

    public Story(String title, Artist artist) {
        super(title, artist, Discipline.STORIES);
    }

}
