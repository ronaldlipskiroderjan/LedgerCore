package com.ronald.LedgerCore.dto.request;

public record LoginRequestDTO(
        String cpf,
        String password
) {
}
