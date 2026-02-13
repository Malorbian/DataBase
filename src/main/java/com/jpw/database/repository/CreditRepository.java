package com.jpw.database.repository;

import com.jpw.database.model.Credit;
import com.jpw.database.model.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CreditRepository extends JpaRepository<Credit, UUID> {

    // Cache: all credits for a collection of media IDs
    List<Credit> findByMediaIdIn(Collection<UUID> mediaIds);

    // Detail:
    List<Credit> findByMediaId(UUID mediaId);

    Optional<Credit> findByMediaIdAndPersonIdAndRole(UUID mediaId, UUID personId, Role role);

    long deleteByMediaId(UUID mediaId);
}

