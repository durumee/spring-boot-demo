package com.nrzm.demo.auth.http;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String msg = "";

        if (authException instanceof InternalAuthenticationServiceException) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            msg = "Internal Server Error";
        } else if (authException instanceof UsernameNotFoundException || authException instanceof BadCredentialsException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            msg = "Invalid username or password";
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            msg = "Unauthorized";
        }

        String errorMsg = """
                { "error" : "%s" }
                """.formatted(msg);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(errorMsg);
    }
}