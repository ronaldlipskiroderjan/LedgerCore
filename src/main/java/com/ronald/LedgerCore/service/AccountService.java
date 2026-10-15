package com.ronald.LedgerCore.service;

import com.ronald.LedgerCore.database.repository.AccountsRepository;
import com.ronald.LedgerCore.dto.response.AccountResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountsRepository accountsRepository;

    public AccountResponseDTO findByOwnerUsername(Authentication authentication) {
        return accountsRepository.findByOwnerCpf(authentication.getName())
                .map(a ->
                        new AccountResponseDTO(
                                a.getId(),
                                a.getOwner().getName(),
                                a.getBalance(),
                                a.getCreateAt(),
                                a.getUpdatedAt()
                        )).orElseThrow(() -> new UsernameNotFoundException("User Not Found!"));
    }
}