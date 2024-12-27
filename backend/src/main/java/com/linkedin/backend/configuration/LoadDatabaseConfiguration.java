package com.linkedin.backend.configuration;

import com.linkedin.backend.features.authentication.model.AuthenticationUser;
import com.linkedin.backend.features.authentication.repository.AuthenticationUserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class LoadDatabaseConfiguration {
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase(AuthenticationUserRepository authenticationUserRepository){
        return args -> {
            if(authenticationUserRepository.findById(1L).isEmpty()){
                AuthenticationUser authenticationUser = AuthenticationUser.builder()
                        .email("nrin31266@yopmail.com")
                        .password(passwordEncoder.encode("123"))
                        .build();
                authenticationUserRepository.save(authenticationUser);
                log.warn("Admin created, please change password!");
            }
        };
    }
}
