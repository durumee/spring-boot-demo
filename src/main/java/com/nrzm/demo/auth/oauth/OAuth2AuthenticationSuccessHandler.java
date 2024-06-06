package com.nrzm.demo.auth.oauth;

import com.nrzm.demo.auth.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;

    public OAuth2AuthenticationSuccessHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        
        // GitHub로부터 받은 사용자 정보
        String id = String.valueOf(attributes.get("id"));
        String name = (String) attributes.get("name");
        String avatarUrl = (String) attributes.get("avatar_url");

        /**
         * TODO: 100. User 확인
         * TODO: 200. User save
         * 300. token 생성
         */
        // JWT 토큰 생성
        final String token = jwtProvider.createToken(name);

        // JWT 리프레시 토큰 생성
        final String refreshToken = jwtProvider.createRefreshToken(name);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/refresh-token");
        response.addCookie(refreshTokenCookie);

        getRedirectStrategy().sendRedirect(request, response, "http://localhost:5173/login?token=" + token);
    }
}
