package com.linkedin.backend.features.authentication.service;

import com.linkedin.backend.exception.AppException;
import com.linkedin.backend.exception.ErrorCode;
import com.linkedin.backend.features.authentication.dto.request.AuthenticationUserRequestBody;
import com.linkedin.backend.features.authentication.model.AuthenticationUser;
import com.linkedin.backend.features.authentication.repository.AuthenticationUserRepository;
import com.linkedin.backend.features.authentication.dto.response.AuthenticationUserResponseBody;
import com.linkedin.backend.features.authentication.utils.Encoder;
import com.linkedin.backend.features.authentication.utils.JsonWebToken;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationUserService {
    AuthenticationUserRepository authenticationUserRepository;
    Encoder encoder;
    JsonWebToken jsonWebToken;

    public Object getUser() {
        return authenticationUserRepository.findByEmail("nrin31266@gmail.com").orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public AuthenticationUserResponseBody register(AuthenticationUserRequestBody authenticationUserRequestBody) {
        try{
            AuthenticationUser authenticationUser = authenticationUserRepository.save(
                    AuthenticationUser.builder()
                            .email(authenticationUserRequestBody.getEmail())
                            .password(encoder.encodePassword(authenticationUserRequestBody.getPassword()))
                            .build()
            );

            return AuthenticationUserResponseBody.builder()
                    .token(jsonWebToken.generateToken(authenticationUser))
                    .message("User registered successfully")
                    .build();
        }catch (Exception e){
            throw new RuntimeException("Cannot register user");
        }
    }

    public AuthenticationUserResponseBody login(AuthenticationUserRequestBody authenticationUserRequestBody) {
        AuthenticationUser authenticationUser = authenticationUserRepository.findByEmail(authenticationUserRequestBody.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if(!encoder.matches(authenticationUserRequestBody.getPassword(), authenticationUser.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }
        return AuthenticationUserResponseBody.builder()
                .token(jsonWebToken.generateToken(authenticationUser))
                .message("User logged in successfully")
                .build();
    }
}