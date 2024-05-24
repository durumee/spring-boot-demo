package com.nrzm.demo_web_db_sec.config;

import com.nrzm.demo_web_db_sec.security.CustomAccessDeniedHandler;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화 (JWT로그인으로 대체)
                .formLogin(form -> form.disable())  //formLogin 비활성화 (JWT 로그인으로 대체)
                .logout(logout -> logout.disable()) // logout 비활성화 (JWT 토큰 만료로 대체)
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandler())
                )
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
