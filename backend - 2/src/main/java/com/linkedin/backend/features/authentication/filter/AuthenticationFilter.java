package com.linkedin.backend.features.authentication.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linkedin.backend.dto.ApiResponse;
import com.linkedin.backend.features.authentication.model.User;
import com.linkedin.backend.features.authentication.service.AuthenticationUserService;
import com.linkedin.backend.features.authentication.utils.JsonWebToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationFilter extends HttpFilter {
    private final List<String> unsecuredGetEndpoints = Arrays.asList(
    );

    private final List<String> unsecuredPostEndpoints = Arrays.asList(
            "/authentication/login",
            "/authentication/register"
    );

    private final List<String> unsecuredPutEndpoints = Arrays.asList(
    );

    private final List<String> unsecuredDeleteEndpoints = Arrays.asList(
    );

    JsonWebToken jsonWebTokenService;
    AuthenticationUserService authenticationUserService;


    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = request.getRequestURI().substring(request.getContextPath().length());
        String method = request.getMethod();
        if (isUnsecuredEndpoint(uri, method)) {
            chain.doFilter(request, response);
            return;
        }
        try{
            String authorizationHeader = request.getHeader("Authorization");
            if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new ServletException("Token missing");
            }
            String token = authorizationHeader.substring(7);

            if(jsonWebTokenService.isTokenExpired(token)) {
                throw new ServletException("Token expired");
            }
            String email = jsonWebTokenService.getEmailFromToken(token);
            User user = authenticationUserService.getUser(email);
            request.setAttribute("authenticatedUser", user);
            chain.doFilter(request, response);
        }catch (Exception e){
            log.error(e.toString());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(ApiResponse.builder()
                    .code(5000)
                    .message("Unauthenticated")
                    .build());
            response.getWriter().write(jsonResponse);
        }

    }

    private boolean isUnsecuredEndpoint(String uri, String method) {
        return switch (method.toUpperCase()) {
            case "GET" -> unsecuredGetEndpoints.contains(uri);
            case "POST" -> unsecuredPostEndpoints.contains(uri);
            case "PUT" -> unsecuredPutEndpoints.contains(uri);
            case "DELETE" -> unsecuredDeleteEndpoints.contains(uri);
            default -> false;
        };
    }


}
