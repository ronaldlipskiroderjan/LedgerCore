package com.ronald.LedgerCore.dto.response;

public record TokenResponseDTO(
        String token,
        long expirationTime
) {
}
