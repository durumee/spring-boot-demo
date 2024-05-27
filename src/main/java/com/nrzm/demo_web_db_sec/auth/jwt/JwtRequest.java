package com.nrzm.demo_web_db_sec.auth.jwt;


import lombok.*;

@Getter
@Setter
@Builder
public class JwtRequest {
    private String username;
    private String password;
}
