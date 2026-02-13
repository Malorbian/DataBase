package com.jpw.database.repository;

import com.jpw.database.model.Franchise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FranchiseRepository extends JpaRepository<Franchise, UUID> {}

