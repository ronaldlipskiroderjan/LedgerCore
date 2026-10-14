package com.ronald.LedgerCore.database.repository;

import com.ronald.LedgerCore.database.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, UUID> {
}
