package com.nrzm.demo.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();

        // 특정 URI에 대해 필터를 건너뛴다.
        if (uri.equals("/invalidate-token")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 100. 요청 헤더에 액세스 토큰을 검증
        String token = jwtProvider.resolveToken(request);

        try {
            if (token != null && jwtProvider.validateToken(token)) {
                SecurityContextHolder.getContext().setAuthentication(jwtProvider.getAuthentication(token, request));
            } else {
                // 300. 액세스 토큰이 무효하면, http only 쿠키에 저장된 리프레시 토큰을 검증
                String refreshToken = jwtProvider.resolveRefreshToken(request);
                if (refreshToken != null && jwtProvider.validateToken(refreshToken)) {
                    String username = jwtProvider.getUsernameFromToken(refreshToken);
                    String newAccessToken = jwtProvider.createToken(username);
                    response.setHeader("Authorization", "Bearer " + newAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(jwtProvider.getAuthentication(newAccessToken, request));
                } else if (refreshToken != null) {
                    // 리프레시 토큰도 무효하면 예외 발생
                    throw new AuthenticationException("Refresh token is invalid or expired") {
                    };
                }
            }
            filterChain.doFilter(request, response);
        } catch (AuthenticationException ex) {
            SecurityContextHolder.clearContext();
            // 예외를 발생시켜 ExceptionTranslationFilter에 의해 처리되도록 함
            request.setAttribute("exception", ex);
            throw ex;
        }
    }
}
