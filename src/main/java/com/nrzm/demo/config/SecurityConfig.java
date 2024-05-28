package com.nrzm.demo.config;

import com.nrzm.demo.auth.jwt.JwtAuthenticationFilter;
import com.nrzm.demo.auth.jwt.JwtProvider;
import com.nrzm.demo.security.CustomAccessDeniedHandler;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtProvider jwtProvider, @Lazy UserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173", "http://localhost:8080")); // 허용할 출처를 명시적으로 지정
//        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // 모든 출처 허용 패턴 지정
        configuration.setAllowedMethods(Arrays.asList("*")); // 모든 HTTP 메서드 허용
        configuration.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 자격 증명 허용
        configuration.addExposedHeader("Authorization");    // Expose Authorization header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 대해 CORS 설정 적용

        return source;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            if (authException instanceof InternalAuthenticationServiceException) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"error\": \"Internal Server Error\"}");
            } else if (authException instanceof UsernameNotFoundException || authException instanceof BadCredentialsException) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"error\": \"Invalid username or password\"}");
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.getWriter().write("{\"error\": \"Unauthorized\"}");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers("/token").permitAll()  //토큰생성(로그인)
                        .requestMatchers("/invalidate-token").permitAll()   //토큰삭제(리프레시토큰까지 제거)
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화 (JWT로그인으로 대체)
                .formLogin(form -> form.disable())  //formLogin 비활성화 (JWT 로그인으로 대체)
                .logout(logout -> logout.disable()) // logout 비활성화 (JWT 토큰 만료로 대체)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler())   // 403 Forbidden 처리 관련
                        .authenticationEntryPoint(authenticationEntryPoint())   // 401 Unauthorized, 500 Internal Server Error 처리 관련
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // CSRF 비활성화
                .headers(headers -> headers
                        //h2 db console 접속 시 클릭재킹 방지를 위해 X-Frame-Options 헤더가 DENY로 설정되어 아래 옵션으로 허용
                        .frameOptions(frameOptions -> frameOptions.disable()) // 클릭재킹 차단 해제
                );
        return http.build();
    }

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
