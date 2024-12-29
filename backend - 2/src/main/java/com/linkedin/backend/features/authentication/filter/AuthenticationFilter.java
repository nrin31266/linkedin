package com.linkedin.backend.features.authentication.filter;

import com.linkedin.backend.features.authentication.service.AuthenticationUserService;
import com.linkedin.backend.features.authentication.utils.JsonWebToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationFilter {
//    private final List<String> unsecuredEndpoints = Arrays.asList(
//            "/authentication/login",
//            "/authentication/register",
//            "/authentication/send-password-reset-token",
//            "/authentication/reset-password");
//
//    JsonWebToken jsonWebToken;
//    AuthenticationUserService authenticationUserService;
//    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws IOException, ServletException {
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        response.addHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
//
//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//            return;
//        }
//
//        String path = request.getRequestURI();
//        if (unsecuredEndpoints.contains(path)) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            String authorization = request.getHeader("Authorization");
//            if (authorization == null || !authorization.startsWith("Bearer ")) {
//                throw new ServletException("Token missing.");
//            }
//
//            String token = authorization.substring(7);
//
//            if (jsonWebToken.isTokenExpired(token)) {
//                throw new ServletException("Invalid token");
//            }
//
//            String email = jsonWebTokenService.getEmailFromToken(token);
//            User user = authenticationService.getUser(email);
//            request.setAttribute("authenticatedUser", user);
//            chain.doFilter(request, response);
//        } catch (Exception e) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json");
//            response.getWriter().write("{\"message\": \"Invalid authentication token, or token missing.\"}");
//        }
//    }

}
