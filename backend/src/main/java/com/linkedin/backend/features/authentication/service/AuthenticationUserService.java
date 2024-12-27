package com.linkedin.backend.features.authentication.service;

import com.linkedin.backend.exception.AppException;
import com.linkedin.backend.exception.ErrorCode;
import com.linkedin.backend.features.authentication.dto.request.AuthenticationUserRequestBody;
import com.linkedin.backend.features.authentication.model.AuthenticationUser;
import com.linkedin.backend.features.authentication.repository.AuthenticationUserRepository;
import com.linkedin.backend.features.authentication.dto.response.AuthenticationUserResponseBody;
import com.linkedin.backend.utils.JsonWebToken;
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
    JsonWebToken jsonWebToken;

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

    public AuthenticationUserResponseBody login(AuthenticationUserRequestBody authenticationUserRequestBody) {
        AuthenticationUser authenticationUser = authenticationUserRepository.findByEmail(authenticationUserRequestBody.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(!passwordEncoder.matches(authenticationUserRequestBody.getPassword(), authenticationUser.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }
        return AuthenticationUserResponseBody.builder()
                .token(jsonWebToken.generateToken(authenticationUser))
                .message("User logged in successfully")
                .build();
    }
}
