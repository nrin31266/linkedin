package com.linkedin.backend.features.authentication.controller;


import com.linkedin.backend.features.authentication.service.AuthenticationUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authentication")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationUserService authenticationUserService;


    @GetMapping("/user")
    public Object getUser() {
        return authenticationUserService.getUser("nrin31266@yopmail.com");
    }
}
