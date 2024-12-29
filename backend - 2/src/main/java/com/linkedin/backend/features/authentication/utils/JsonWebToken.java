package com.linkedin.backend.features.authentication.utils;


import com.linkedin.backend.features.authentication.model.AuthenticationUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JsonWebToken {
    @Value("${jwt.signerKey}")
    @NonFinal
    protected String SIGNER_KEY;

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SIGNER_KEY.getBytes());
    }

    public String generateToken(AuthenticationUser user) {
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getEmail())
                .issuedAt(new Date())
                .expiration(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .signWith(getKey())
                .claim("vanin05", "Hello friend, welcome to linkedin")
                .compact();
    }


    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseEncryptedClaims(token)
                .getPayload();
    }
}
