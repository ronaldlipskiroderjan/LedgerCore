package com.ronald.LedgerCore.database.repository;

import com.ronald.LedgerCore.database.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountsRepository extends JpaRepository<AccountEntity, UUID> {
    Optional<AccountEntity> findByOwnerCpf(String cpf);
}
