package com.jpw.database.repository;

import com.jpw.database.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SeriesRepository extends JpaRepository<Series, UUID> {}

