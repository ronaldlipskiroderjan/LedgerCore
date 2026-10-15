package com.ronald.LedgerCore.database.repository;

import com.ronald.LedgerCore.database.model.EntrieEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EntriesRepository extends JpaRepository<EntrieEntity, UUID> {
}
