package com.jpw.database.model.mediaTypes;

import com.jpw.database.model.enums.Discipline;
import com.jpw.database.model.enums.VideoType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@PrimaryKeyJoinColumn(name = "media_id")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Video extends MediaBase {

    @Setter
    private Double lengthInMinutes;

    @Setter
    @Enumerated(EnumType.STRING)
    private VideoType videoType;

    @Setter
    private UUID seriesId;


    public Video(UUID id, String title) {
        super(id, title, Discipline.VIDEO);
    }

}
