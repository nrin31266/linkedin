package com.linkedin.backend.features.authentication.controller;


import com.linkedin.backend.dto.ApiResponse;
import com.linkedin.backend.features.authentication.dto.request.AuthenticationUserRequestBody;
import com.linkedin.backend.features.authentication.dto.response.AuthenticationUserResponseBody;
import com.linkedin.backend.features.authentication.service.AuthenticationUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationUserService authenticationUserService;


    @GetMapping("/user")
    public ApiResponse<Object> getUser() {
        return ApiResponse.builder()
                .data(authenticationUserService.getUser())
                .build();
    }

    @PostMapping("/register")
    public ApiResponse<AuthenticationUserResponseBody> register(@RequestBody @Validated AuthenticationUserRequestBody authenticationUserRequestBody) {
        return ApiResponse.<AuthenticationUserResponseBody>builder()
                .data(authenticationUserService.register(authenticationUserRequestBody))
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<AuthenticationUserResponseBody> login(@RequestBody @Validated AuthenticationUserRequestBody authenticationUserRequestBody) {
        return ApiResponse.<AuthenticationUserResponseBody>builder()
                .data(authenticationUserService.login(authenticationUserRequestBody))
                .build();
    }
}
