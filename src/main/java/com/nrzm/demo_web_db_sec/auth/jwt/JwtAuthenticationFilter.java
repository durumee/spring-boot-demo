package com.nrzm.demo_web_db_sec.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 100. 요청 헤더에 액세스 토큰을 검증
        String token = jwtProvider.resolveToken(request);

        // 200. 액세스 토큰이 유효하면 통과
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
                //리프레시 토큰도 무효하면 http 401 오류 코드를 응답한다
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Refresh token is invalid or expired");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
