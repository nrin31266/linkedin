package com.linkedin.backend.configuration;

import com.linkedin.backend.exception.AppException;
import com.linkedin.backend.exception.ErrorCode;
import com.linkedin.backend.utils.JsonWebToken;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {

    private final JsonWebToken jsonWebToken;

    public CustomJwtDecoder(JsonWebToken jsonWebToken) {
        this.jsonWebToken = jsonWebToken;
    }

    @Override
    public Jwt decode(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean isValidToken = jsonWebToken.isValidToken(token);
            if (!isValidToken) {
                throw new JwtException("Invalid token");
            }

            Jwt jwt = new Jwt(
                    token,
                    signedJWT.getJWTClaimsSet().getIssueTime().toInstant(),
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant(),
                    signedJWT.getHeader().toJSONObject(),
                    signedJWT.getJWTClaimsSet().getClaims()
            );


            return jwt;
        } catch (ParseException | JOSEException e) {
            throw new JwtException(e.getMessage());
        }
    }
}