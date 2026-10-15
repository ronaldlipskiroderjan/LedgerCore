package com.ronald.LedgerCore.controller;

import com.ronald.LedgerCore.dto.response.AccountResponseDTO;
import com.ronald.LedgerCore.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AccountResponseDTO findByUsername(Authentication authentication) {
        return accountService.findByOwnerUsername(authentication);
    }
}
