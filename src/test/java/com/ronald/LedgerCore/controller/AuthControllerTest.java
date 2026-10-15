package com.ronald.LedgerCore.controller;

import com.ronald.LedgerCore.dto.request.LoginRequestDTO;
import com.ronald.LedgerCore.dto.request.UserRequestDTO;
import com.ronald.LedgerCore.dto.response.TokenResponseDTO;
import com.ronald.LedgerCore.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(authController).build();
    }

    @Test
    void registerShouldCreateUserAndReturnCreated() throws Exception {
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Ronald",
                                  "password": "strong-password",
                                  "cpf": "12345678900"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));

        ArgumentCaptor<UserRequestDTO> dtoCaptor = ArgumentCaptor.forClass(UserRequestDTO.class);
        verify(authService).register(dtoCaptor.capture());
        assertThat(dtoCaptor.getValue())
                .isEqualTo(new UserRequestDTO("Ronald", "strong-password", "12345678900"));
        verifyNoMoreInteractions(authService);
    }

    @Test
    void loginShouldReturnTokenAndExpirationTime() throws Exception {
        LoginRequestDTO request = new LoginRequestDTO("12345678900", "strong-password");
        TokenResponseDTO response = new TokenResponseDTO("generated-token", 900_000L);
        when(authService.login(request)).thenReturn(response);

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "cpf": "12345678900",
                                  "password": "strong-password"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("generated-token"))
                .andExpect(jsonPath("$.expirationTime").value(900_000));

        verify(authService).login(request);
        verifyNoMoreInteractions(authService);
    }

    @Test
    void refreshTokenShouldUseCurrentAuthenticationAndReturnNewToken() throws Exception {
        TokenResponseDTO response = new TokenResponseDTO("refreshed-token", 900_000L);
        when(authService.refreshToken(authentication)).thenReturn(response);

        mockMvc.perform(post("/v1/auth/refresh").principal(authentication))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("refreshed-token"))
                .andExpect(jsonPath("$.expirationTime").value(900_000));

        verify(authService).refreshToken(authentication);
        verifyNoMoreInteractions(authService);
    }
}
