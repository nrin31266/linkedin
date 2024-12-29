package com.linkedin.backend.configuration;

import com.linkedin.backend.features.authentication.model.User;
import com.linkedin.backend.features.authentication.repository.AuthenticationUserRepository;
import com.linkedin.backend.features.authentication.utils.Encoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class LoadDatabaseConfiguration {
    private final Encoder encoder;

    @Bean
    CommandLineRunner initDatabase(AuthenticationUserRepository authenticationUserRepository){
        return args -> {
            if(authenticationUserRepository.findById(1L).isEmpty()){
                User authenticationUser = User.builder()
                        .email("nrin31266@yopmail.com")
                        .password(encoder.encodePassword("123"))
                        .build();
                authenticationUserRepository.save(authenticationUser);
                log.warn("Admin created, please change password!");
            }
        };
    }
}
