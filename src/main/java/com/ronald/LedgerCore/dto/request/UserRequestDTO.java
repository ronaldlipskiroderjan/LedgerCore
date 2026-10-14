package com.ronald.LedgerCore.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDTO(
        @NotBlank(message = "The CPF cannot be Empty.")
        String name,

        @NotBlank(message = "The CPF cannot be Empty.")
        String password,

        @NotBlank(message = "The CPF cannot be Empty.")
        String cpf
) {
}
