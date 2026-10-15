package com.ronald.LedgerCore.database.model;

import com.ronald.LedgerCore.enums.EntryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntrieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id",
                nullable = false
    )
    private AccountEntity account;

    @ManyToOne
    @JoinColumn(name = "transaction_id",
                nullable = false
    )
    private TransactionEntity transaction;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type",
            nullable = false
    )
    private EntryType entryType;

    @Column(nullable = false)
    @Size(min = 2, max = 19)
    private BigDecimal amount;

    @Column(name = "create_at",
            nullable = false
    )
    private LocalDateTime createAt;
}
