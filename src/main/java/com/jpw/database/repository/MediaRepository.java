package com.jpw.database.repository;

import com.jpw.database.model.enums.Discipline;
import com.jpw.database.model.mediaTypes.MediaBase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MediaRepository extends JpaRepository<MediaBase, UUID> {

    // Tab-Cache: load all media for a discipline at once
    List<MediaBase> findByDiscipline(Discipline discipline);
}

