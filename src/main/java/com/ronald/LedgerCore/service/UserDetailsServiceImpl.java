package com.ronald.LedgerCore.service;

import com.ronald.LedgerCore.database.model.UserEntity;
import com.ronald.LedgerCore.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return buscarUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado..."));
    }

    private Optional<UserEntity> buscarUsuario(
            String identificador
    ) {
        try {
            return userRepository.findById(UUID.fromString(identificador));
        } catch (IllegalArgumentException ex) {
            return userRepository.findByCpf(identificador);
        }
    }
}
