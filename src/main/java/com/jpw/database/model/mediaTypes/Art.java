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
public class Art extends MediaBase {

    @Setter
    private String medium;

    public Art(UUID id, String title) {
        super(id, title, Discipline.ART);
    }

}
