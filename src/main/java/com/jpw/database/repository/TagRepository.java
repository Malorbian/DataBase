package com.jpw.database.repository;

import com.jpw.database.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TagRepository extends JpaRepository<Tag, UUID> {

    Optional<Tag> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}

