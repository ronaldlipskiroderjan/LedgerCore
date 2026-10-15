package com.ronald.LedgerCore.controller;

import com.ronald.LedgerCore.dto.response.AccountResponseDTO;
import com.ronald.LedgerCore.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(accountController).build();
    }

    @Test
    void findByUsernameShouldReturnAuthenticatedOwnersAccount() throws Exception {
        UUID accountId = UUID.fromString("b4809948-814a-4d6f-938d-50b11b14d8ae");
        LocalDateTime createdAt = LocalDateTime.of(2026, 10, 15, 18, 30);
        LocalDateTime updatedAt = LocalDateTime.of(2026, 10, 15, 19, 45);
        AccountResponseDTO response = new AccountResponseDTO(
                accountId,
                "Ronald",
                new BigDecimal("1250.75"),
                createdAt,
                updatedAt
        );
        when(accountService.findByOwnerUsername(authentication)).thenReturn(response);

        mockMvc.perform(get("/v1/account").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(accountId.toString()))
                .andExpect(jsonPath("$.ownerName").value("Ronald"))
                .andExpect(jsonPath("$.balance").value(1250.75))
                .andExpect(jsonPath("$.createAt").value("2026-10-15T18:30:00"))
                .andExpect(jsonPath("$.updateAt").value("2026-10-15T19:45:00"));

        verify(accountService).findByOwnerUsername(authentication);
        verifyNoMoreInteractions(accountService);
    }

    @Test
    void findByUsernameShouldPropagateServiceExceptionWhenAccountDoesNotExist() {
        UsernameNotFoundException failure = new UsernameNotFoundException("User Not Found!");
        when(accountService.findByOwnerUsername(authentication)).thenThrow(failure);

        assertThatThrownBy(() -> accountController.findByUsername(authentication))
                .isSameAs(failure);

        verify(accountService).findByOwnerUsername(authentication);
        verifyNoMoreInteractions(accountService);
    }
}
