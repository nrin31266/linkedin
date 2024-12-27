package com.linkedin.backend.utils;

import com.linkedin.backend.features.authentication.model.AuthenticationUser;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.text.ParseException;
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

    public String generateToken(AuthenticationUser authenticationUser) {
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(authenticationUser.getEmail())
                .issuer("vanrin05.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
//                .claim("scope", buildScope(user))
                .claim("vanrin05", "Hello, welcome to linkedin")
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Can't generate JWT token", e);
        }
    }

    public boolean isExpired(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
        return expirationDate.before(new Date());
    }

    public boolean isSignerKey(String token) throws JOSEException, ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        return signedJWT.verify(verifier);
    }

    public boolean isValidToken(String token) throws ParseException, JOSEException {
        if(!isSignerKey(token)){
            return false;
        }
        if(isExpired(token)){
            return false;
        }
        return true;
    }
}
