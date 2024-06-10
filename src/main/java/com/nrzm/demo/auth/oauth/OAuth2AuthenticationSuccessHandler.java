package com.nrzm.demo.auth.oauth;

import com.nrzm.demo.auth.jwt.JwtProvider;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
    private final InMemoryUserDetailsManager userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public OAuth2AuthenticationSuccessHandler(JwtProvider jwtProvider, InMemoryUserDetailsManager userDetailsService, PasswordEncoder passwordEncoder) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        
        // GitHub로부터 받은 사용자 정보
        String id = String.valueOf(attributes.get("id"));
        String name = (String) attributes.get("name");
        String email = (String) attributes.get("email");
        String avatarUrl = (String) attributes.get("avatar_url");
        String registId = authToken.getAuthorizedClientRegistrationId() + "_" + id;

        log.debug("name, email :: {}, {}", name, email);

        /**
         * 100. User 확인
         * 200. User save
         * 300. token 생성
         */
        // User 확인 및 save (by InMemoryUserDetailsManager)
        if (!userDetailsService.userExists(registId)) {
            UserDetails user = User.builder()
                    .username(registId)
                    .password(passwordEncoder.encode("1234"))
                    .roles("USER")
                    .build();
            userDetailsService.createUser(user);
        }

        // JWT 토큰 생성
        final String token = jwtProvider.createToken(registId);

        // JWT 리프레시 토큰 생성
        final String refreshToken = jwtProvider.createRefreshToken(registId);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/refresh-token");
        response.addCookie(refreshTokenCookie);

        getRedirectStrategy().sendRedirect(request, response, "http://localhost:5173/login?token=" + token);
    }
}
