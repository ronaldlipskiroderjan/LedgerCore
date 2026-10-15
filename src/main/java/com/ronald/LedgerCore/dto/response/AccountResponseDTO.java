package com.ronald.LedgerCore.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record AccountResponseDTO(
        UUID id,
        String ownerName,
        BigDecimal balance,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
}
