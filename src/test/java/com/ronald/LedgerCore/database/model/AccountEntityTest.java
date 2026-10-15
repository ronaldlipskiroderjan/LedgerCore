package com.ronald.LedgerCore.database.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class AccountEntityTest {

    @Mock
    private EntrieEntity entry;

    @Mock
    private UserEntity owner;

    @Test
    void builderShouldCreateAccountWithAllProvidedFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.of(2026, 10, 14, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 10, 14, 11, 0);
        BigDecimal balance = new BigDecimal("250.75");
        Set<EntrieEntity> entries = Set.of(entry);

        AccountEntity account = AccountEntity.builder()
                .id(id)
                .owner(owner)
                .balance(balance)
                .createAt(createdAt)
                .updatedAt(updatedAt)
                .entries(entries)
                .build();

        assertAll(
                () -> assertEquals(id, account.getId()),
                () -> assertSame(owner, account.getOwner()),
                () -> assertEquals(balance, account.getBalance()),
                () -> assertEquals(createdAt, account.getCreateAt()),
                () -> assertEquals(updatedAt, account.getUpdatedAt()),
                () -> assertSame(entries, account.getEntries()),
                () -> assertSame(entry, account.getEntries().iterator().next())
        );
    }

    @Test
    void builderShouldUseZeroAsDefaultBalance() {
        AccountEntity account = AccountEntity.builder()
                .owner(owner)
                .build();

        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    void noArgsConstructorShouldUseZeroAsDefaultBalance() {
        AccountEntity account = new AccountEntity();

        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @Test
    void constructorsShouldInitializeEntriesAsEmptySet() {
        AccountEntity accountBuilt = AccountEntity.builder().owner(owner).build();
        AccountEntity accountCreatedByJpa = new AccountEntity();

        assertAll(
                () -> assertTrue(accountBuilt.getEntries().isEmpty()),
                () -> assertTrue(accountCreatedByJpa.getEntries().isEmpty())
        );
    }

    @Test
    void settersShouldUpdateMutableFields() {
        AccountEntity account = new AccountEntity();
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.of(2026, 10, 14, 12, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 10, 14, 13, 0);
        BigDecimal balance = new BigDecimal("99.90");
        Set<EntrieEntity> entries = new HashSet<>(Set.of(entry));

        account.setId(id);
        account.setOwner(owner);
        account.setBalance(balance);
        account.setCreateAt(createdAt);
        account.setUpdatedAt(updatedAt);
        account.setEntries(entries);

        assertAll(
                () -> assertEquals(id, account.getId()),
                () -> assertSame(owner, account.getOwner()),
                () -> assertEquals(balance, account.getBalance()),
                () -> assertEquals(createdAt, account.getCreateAt()),
                () -> assertEquals(updatedAt, account.getUpdatedAt()),
                () -> assertSame(entries, account.getEntries())
        );
    }

    @Test
    void addEntryShouldSynchronizeBothSidesOfRelationship() {
        AccountEntity account = new AccountEntity();

        account.addEntry(entry);

        assertTrue(account.getEntries().contains(entry));
        verify(entry).setAccount(account);
    }

    @Test
    void removeEntryShouldSynchronizeBothSidesOfRelationship() {
        AccountEntity account = new AccountEntity();
        account.getEntries().add(entry);

        account.removeEntry(entry);

        assertFalse(account.getEntries().contains(entry));
        verify(entry).setAccount(null);
    }

    @Test
    void removeEntryShouldNotChangeUnassociatedEntry() {
        AccountEntity account = new AccountEntity();

        account.removeEntry(entry);

        assertTrue(account.getEntries().isEmpty());
        verifyNoInteractions(entry);
    }

    @Test
    void mappingsShouldUseSingleOwningSideAndMappedByInverseSides() throws NoSuchFieldException {
        Field ownerField = AccountEntity.class.getDeclaredField("owner");
        OneToOne ownerRelation = ownerField.getAnnotation(OneToOne.class);
        JoinColumn ownerJoinColumn = ownerField.getAnnotation(JoinColumn.class);

        Field userAccountField = UserEntity.class.getDeclaredField("accountEntity");
        OneToOne userAccountRelation = userAccountField.getAnnotation(OneToOne.class);

        Field entriesField = AccountEntity.class.getDeclaredField("entries");
        OneToMany entriesRelation = entriesField.getAnnotation(OneToMany.class);

        assertAll(
                () -> assertFalse(ownerRelation.optional()),
                () -> assertEquals("owner_id", ownerJoinColumn.name()),
                () -> assertFalse(ownerJoinColumn.nullable()),
                () -> assertTrue(ownerJoinColumn.unique()),
                () -> assertEquals("owner", userAccountRelation.mappedBy()),
                () -> assertEquals("account", entriesRelation.mappedBy()),
                () -> assertTrue(entriesRelation.orphanRemoval())
        );
    }
}
