package com.ronald.LedgerCore.service;

import com.ronald.LedgerCore.config.TokenProvider;
import com.ronald.LedgerCore.database.model.AccountEntity;
import com.ronald.LedgerCore.database.model.RoleEntity;
import com.ronald.LedgerCore.database.model.UserEntity;
import com.ronald.LedgerCore.database.repository.AccountsRepository;
import com.ronald.LedgerCore.database.repository.RoleRepository;
import com.ronald.LedgerCore.database.repository.UserRepository;
import com.ronald.LedgerCore.dto.request.LoginRequestDTO;
import com.ronald.LedgerCore.dto.request.UserRequestDTO;
import com.ronald.LedgerCore.dto.response.TokenResponseDTO;
import com.ronald.LedgerCore.enums.RoleTypeEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AccountsRepository accountsRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    @Value("${jwt.expiration}")
    private long expirationTime;

    @Transactional
    public void register(UserRequestDTO dto) throws Exception{
        if(userRepository.existsByCpf(dto.password())) {
            throw new BadRequestException("Usuário não encontrado.");
        }
        RoleEntity newRole = roleRepository.findByName(RoleTypeEnum.ROLE_USER.name())
                .orElseGet(() -> roleRepository.save(RoleEntity.builder()
                                .name(RoleTypeEnum.ROLE_ADMIN.name())
                        .build()));
        UserEntity newUser = (UserEntity.builder()
                        .name(dto.name())
                        .password(passwordEncoder.encode(dto.password()))
                        .cpf(dto.cpf())
                        .roles(Set.of(newRole))
                .build());
        userRepository.save(newUser);
        accountsRepository.save(AccountEntity.builder()
                        .owner(newUser)
                .build());
    }

    public TokenResponseDTO login(LoginRequestDTO dto) throws Exception {
        try {
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.cpf().toLowerCase(), dto.password()));
            String token = tokenProvider.gerarToken(auth);
            return new TokenResponseDTO(token, expirationTime);
        } catch (BadCredentialsException ex) {
            throw new BadRequestException("Credenciais Inválidas");
        } catch (Exception ex) {
            throw ex;
        }
    }

    public TokenResponseDTO refreshToken(Authentication authentication) {
        return new TokenResponseDTO(tokenProvider.gerarToken(authentication), expirationTime);
    }
}
