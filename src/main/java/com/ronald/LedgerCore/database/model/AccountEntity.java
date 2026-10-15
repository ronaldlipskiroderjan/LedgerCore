package com.ronald.LedgerCore.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false, unique = true)
    private UserEntity owner;

    @Builder.Default
    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "create_at",
            nullable = false
    )
    private LocalDateTime createAt;

    @Column(name = "update_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    @OneToMany(
            mappedBy = "account",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private Set<EntrieEntity> entries = new HashSet<>();

    public void addEntry(EntrieEntity entry) {
        entries.add(entry);
        entry.setAccount(this);
    }

    public void removeEntry(EntrieEntity entry) {
        if (entries.remove(entry)) {
            entry.setAccount(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AccountEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
