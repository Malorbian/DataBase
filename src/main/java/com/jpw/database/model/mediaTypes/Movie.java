package com.jpw.database.model.mediaTypes;

import com.jpw.database.model.Artist;
import com.jpw.database.model.Discipline;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "videos")
@PrimaryKeyJoinColumn(name = "media_id")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Movie extends MediaBase {

    @Setter
    private Double lengthInMinutes;

    @Setter
    private

    public Movie(String title, Artist artist) {
        super(title, artist, Discipline.VIDEOS);
    }

}
