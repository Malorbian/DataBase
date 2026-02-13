package com.jpw.database.repository;

import com.jpw.database.model.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrganisationRepository extends JpaRepository<Organisation, UUID> {}

