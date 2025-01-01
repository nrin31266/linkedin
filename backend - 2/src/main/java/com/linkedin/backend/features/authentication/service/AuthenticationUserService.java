package com.linkedin.backend.features.authentication.service;

import com.linkedin.backend.exception.AppException;
import com.linkedin.backend.exception.ErrorCode;
import com.linkedin.backend.features.authentication.dto.request.*;
import com.linkedin.backend.features.authentication.model.User;
import com.linkedin.backend.features.authentication.repository.AuthenticationUserRepository;
import com.linkedin.backend.features.authentication.dto.response.AuthenticationUserResponseBody;
import com.linkedin.backend.utils.EmailService;
import com.linkedin.backend.features.authentication.utils.Encoder;
import com.linkedin.backend.features.authentication.utils.JsonWebToken;
import com.linkedin.backend.features.authentication.utils.OneTimePasswordGenerator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationUserService {
    AuthenticationUserRepository authenticationUserRepository;
    Encoder encoder;
    JsonWebToken jsonWebToken;
    OneTimePasswordGenerator oneTimePasswordGenerator;
    int durationInMinutes = 1;
    EmailService emailService;

    public User getUser(String email) {
        return authenticationUserRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }


    public AuthenticationUserResponseBody register(AuthenticationUserRequestBody authenticationUserRequestBody) {
        try {
            String emailVerificationCode = oneTimePasswordGenerator.generateOTP();

            User authenticationUser = authenticationUserRepository.save(
                    User.builder()
                            .email(authenticationUserRequestBody.getEmail())
                            .password(encoder.encode(authenticationUserRequestBody.getPassword()))
                            .emailVerificationTokenExpiryDate(new Date(Instant.now().plus(durationInMinutes, ChronoUnit.MINUTES).toEpochMilli()))
                            .emailVerificationToken(encoder.encode(emailVerificationCode))
                            .build()
            );

            emailService.sendEmail(SendEmailRequest.builder()
                    .to(authenticationUserRequestBody.getEmail())
                    .subject("WELCOME TO LINKEDIN CLONE BY RINVAN05")
                    .body(emailVerificationCode)
                    .build());


            return AuthenticationUserResponseBody.builder()
                    .token(jsonWebToken.generateToken(authenticationUser))
                    .message("User registered successfully")
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Cannot register user");
        }
    }

    public AuthenticationUserResponseBody login(AuthenticationUserRequestBody authenticationUserRequestBody) {
        User authenticationUser = authenticationUserRepository.findByEmail(authenticationUserRequestBody.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!encoder.matches(authenticationUserRequestBody.getPassword(), authenticationUser.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }
        return AuthenticationUserResponseBody.builder()
                .token(jsonWebToken.generateToken(authenticationUser))
                .message("User logged in successfully")
                .build();
    }

    public void sendEmailVerifyToken(String email) {
        User user = authenticationUserRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.getEmailVerified() == null || !user.getEmailVerified()) {
            String emailVerificationCode = oneTimePasswordGenerator.generateOTP();
            user.setEmailVerificationToken(encoder.encode(emailVerificationCode));
            user.setEmailVerificationTokenExpiryDate(new Date(Instant.now().plus(durationInMinutes, ChronoUnit.MINUTES).toEpochMilli()));
            authenticationUserRepository.save(user);
            emailService.sendEmail(SendEmailRequest.builder()
                    .to(email)
                    .subject("EMAIL VERIFICATION")
                    .body(emailVerificationCode)
                    .build());
        } else {
            throw new AppException(ErrorCode.EMAIL_ALREADY_VERIFIED);
        }
    }

    public void validateEmailVerificationToken(String token, String email) {
        User user = authenticationUserRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.getEmailVerificationToken() != null && encoder.matches(token, user.getEmailVerificationToken())) {
            if (user.getEmailVerificationTokenExpiryDate().before(new Date())) {
                throw new AppException(ErrorCode.EMAIL_VERIFICATION_EXPIRED);
            }

            user.setEmailVerified(true);
            user.setEmailVerificationToken(null);
            user.setEmailVerificationTokenExpiryDate(null);
            authenticationUserRepository.save(user);
        } else {
            throw new AppException(ErrorCode.EMAIL_VERIFICATION_FAILED);
        }
    }

    public void sendPasswordResetToken(String email) {
        User user = authenticationUserRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        String passwordResetCode = oneTimePasswordGenerator.generateOTP();
        user.setPasswordResetToken(encoder.encode(passwordResetCode));
        user.setPasswordResetTokenExpiryDate(new Date(Instant.now().plus(durationInMinutes, ChronoUnit.MINUTES).toEpochMilli()));
        authenticationUserRepository.save(user);
        emailService.sendEmail(SendEmailRequest.builder()
                .to(email)
                .subject("PASSWORD RESET")
                .body(passwordResetCode)
                .build());
    }

    public void resetPassword(PasswordResetRequest request){
        User user = authenticationUserRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.getPasswordResetToken() != null && encoder.matches(request.getToken(), user.getPasswordResetToken())) {
            if (user.getPasswordResetTokenExpiryDate().before(new Date())) {
                throw new AppException(ErrorCode.PASSWORD_RESET_TOKEN_EXPIRED);
            }
            if(encoder.matches(request.getNewPassword(), user.getPassword())){
                throw new AppException(ErrorCode.NEW_PASSWORD_CAN_NOT_SAME_OLD_PASSWORD);
            }

            user.setPassword(encoder.encode(request.getNewPassword()));
            user.setPasswordResetToken(null);
            user.setPasswordResetTokenExpiryDate(null);
            authenticationUserRepository.save(user);
        } else {
            throw new AppException(ErrorCode.PASSWORD_RESET_FAILED);
        }
    }

}
