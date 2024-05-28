package com.nrzm.demo_web_db_sec.controller;

import com.nrzm.demo_web_db_sec.auth.jwt.JwtProvider;
import com.nrzm.demo_web_db_sec.auth.jwt.JwtRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class JwtController {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    public JwtController(AuthenticationManager authenticationManager, JwtProvider jwtProvider, @Lazy UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> login(@RequestBody JwtRequest jwtRequest, HttpServletResponse response) {
// 사용자 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        jwtRequest.getUsername(),
                        jwtRequest.getPassword()
                )
        );

        // 인증된 사용자 정보 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // JWT 토큰 생성
        final String token = jwtProvider.createToken(userDetails.getUsername());

        // JWT 토큰을 응답 헤더로 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        // JWT 리프레시 토큰 생성
        final String refreshToken = jwtProvider.createRefreshToken(userDetails.getUsername());
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/refresh-token");
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok().headers(headers).build();
    }

    @GetMapping("/invalidate-token")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorizationHeader, HttpServletResponse response) {
        // 리프레시 토큰 무효화 (쿠키 삭제)
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/refresh-token");
        refreshTokenCookie.setMaxAge(0); // 쿠키 삭제
        response.addCookie(refreshTokenCookie);

        return ResponseEntity.ok().build();
    }
}
