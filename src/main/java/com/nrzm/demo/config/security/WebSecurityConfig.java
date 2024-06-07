package com.nrzm.demo.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;

@Configuration
public class WebSecurityConfig {
    // securityFilterChain 대상에서 제외시킴
    // /invalidate-token 의 경우는 Controller 메소드여서인지 제외되지 않아서
    // SecurityConfig 에서 permitAll 처리는 물론 JwtAuthenticationFilter 에서도 별도 예외처리함

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/resources/**",
                "/static/**",
                "/css/**",
                "/js/**",
                "/assets/**",   /*리액트JS 빌드 기본 리소스*/
                "/images/**",
                "/error/**",
                "/",
                "/index.html",
                "/favicon.*"
        );
    }
}
