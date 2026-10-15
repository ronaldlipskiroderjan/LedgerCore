package com.ronald.LedgerCore.database.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MockitoExtension.class)
class AccountEntityTest {

    @Mock
    private EntrieEntity entry;

    @Test
    void builderShouldCreateAccountWithAllProvidedFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.of(2026, 10, 14, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 10, 14, 11, 0);
        BigDecimal balance = new BigDecimal("250.75");
        List<EntrieEntity> entries = List.of(entry);

        AccountEntity account = AccountEntity.builder()
                .id(id)
                .ownerName("Ronald Roderjan")
                .balance(balance)
                .createAt(createdAt)
                .updatedAt(updatedAt)
                .entries(entries)
                .build();

        assertAll(
                () -> assertEquals(id, account.getId()),
                () -> assertEquals("Ronald Roderjan", account.getOwnerName()),
                () -> assertEquals(balance, account.getBalance()),
                () -> assertEquals(createdAt, account.getCreateAt()),
                () -> assertEquals(updatedAt, account.getUpdatedAt()),
                () -> assertSame(entries, account.getEntries()),
                () -> assertSame(entry, account.getEntries().getFirst())
        );
    }

    @Test
    void builderShouldUseZeroAsDefaultBalance() {
        AccountEntity account = AccountEntity.builder()
                .ownerName("Ronald Roderjan")
                .build();

        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    void noArgsConstructorShouldUseZeroAsDefaultBalance() {
        AccountEntity account = new AccountEntity();

        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    void settersShouldUpdateMutableFields() {
        AccountEntity account = new AccountEntity();
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.of(2026, 10, 14, 12, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 10, 14, 13, 0);
        BigDecimal balance = new BigDecimal("99.90");
        List<EntrieEntity> entries = List.of(entry);

        account.setId(id);
        account.setOwnerName("Novo titular");
        account.setBalance(balance);
        account.setCreateAt(createdAt);
        account.setUpdatedAt(updatedAt);
        account.setEntries(entries);

        assertAll(
                () -> assertEquals(id, account.getId()),
                () -> assertEquals("Novo titular", account.getOwnerName()),
                () -> assertEquals(balance, account.getBalance()),
                () -> assertEquals(createdAt, account.getCreateAt()),
                () -> assertEquals(updatedAt, account.getUpdatedAt()),
                () -> assertSame(entries, account.getEntries())
        );
    }
}
