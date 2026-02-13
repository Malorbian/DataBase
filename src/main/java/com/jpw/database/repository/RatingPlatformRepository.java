package com.jpw.database.repository;

import com.jpw.database.model.RatingPlatform;
import com.jpw.database.model.enums.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingPlatformRepository extends JpaRepository<RatingPlatform, UUID> {

    Optional<RatingPlatform> findByNameIgnoreCaseAndDiscipline(String name, Discipline discipline);

    List<RatingPlatform> findByDiscipline(Discipline discipline);
}

