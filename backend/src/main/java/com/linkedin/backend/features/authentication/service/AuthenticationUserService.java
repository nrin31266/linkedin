package com.linkedin.backend.features.authentication.service;

import com.linkedin.backend.features.authentication.dto.request.AuthenticationUserRequestBody;
import com.linkedin.backend.features.authentication.model.AuthenticationUser;
import com.linkedin.backend.features.authentication.repository.AuthenticationUserRepository;
import com.linkedin.backend.features.authentication.dto.response.AuthenticationUserResponseBody;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationUserService {
    AuthenticationUserRepository authenticationUserRepository;
    PasswordEncoder passwordEncoder;

    public Object getUser(String email) {
        return authenticationUserRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public AuthenticationUserResponseBody register(AuthenticationUserRequestBody authenticationUserRequestBody) {
        authenticationUserRepository.save(
                AuthenticationUser.builder()
                        .email(authenticationUserRequestBody.getEmail())
                        .password(passwordEncoder.encode(authenticationUserRequestBody.getPassword()))
                        .build()
        );

        return AuthenticationUserResponseBody.builder()
                .token("token")
                .message("User registered successfully")
                .build();
    }
}
