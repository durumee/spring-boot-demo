package com.nrzm.demo.security;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        String acceptHeader = request.getHeader("Accept");
        String deniedUrl = request.getRequestURI(); //접근 거부된 url 패턴 확인
        log.info("deniedUrl:: {}", deniedUrl);

        if (acceptHeader != null && acceptHeader.contains("application/json")) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String jsonResponse = String.format("{\"error\": \"%s\", \"message\": \"%s\"}", "Forbidden", accessDeniedException.getMessage());

            response.getWriter().write(jsonResponse);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            request.setAttribute("errorMessage", accessDeniedException.getMessage());

            RequestDispatcher dispatcher = request.getRequestDispatcher("/error/403");
            dispatcher.forward(request, response);
        }
    }
}
