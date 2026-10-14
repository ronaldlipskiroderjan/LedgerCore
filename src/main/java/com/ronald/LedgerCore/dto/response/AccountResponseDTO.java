package com.ronald.LedgerCore.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponseDTO(
        String ownerName,
        BigDecimal balance,
        LocalDateTime createAt,
        LocalDateTime updateAt
) {
}
