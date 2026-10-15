package com.ronald.LedgerCore.controller;

import com.ronald.LedgerCore.dto.request.LoginRequestDTO;
import com.ronald.LedgerCore.dto.request.UserRequestDTO;
import com.ronald.LedgerCore.dto.response.TokenResponseDTO;
import com.ronald.LedgerCore.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody UserRequestDTO dto) throws Exception {
        authService.register(dto);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponseDTO login(@Valid @RequestBody LoginRequestDTO dto) throws Exception {
        return authService.login(dto);
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponseDTO refreshToken(Authentication authentication) {
        return authService.refreshToken(authentication);
    }
}
