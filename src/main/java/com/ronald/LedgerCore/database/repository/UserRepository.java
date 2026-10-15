package com.ronald.LedgerCore.database.repository;

import com.ronald.LedgerCore.database.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByCpf(String cpf);
    boolean existsByCpf(String cpf);
}
