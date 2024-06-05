package com.nrzm.demo.auth.oauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        Map<String, Object> attributes = authToken.getPrincipal().getAttributes();
        
        // GitHub로부터 받은 사용자 정보
        String id = String.valueOf(attributes.get("id"));
        String name = (String) attributes.get("name");
        String avatarUrl = (String) attributes.get("avatar_url");

        log.info("succ:: {}, {}, {}", id, name, avatarUrl);

        // TODO: JWT 토큰 생성 로직

        getRedirectStrategy().sendRedirect(request, response, "/");
    }
}
