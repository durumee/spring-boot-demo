package com.nrzm.demo.auth.jwt;


import lombok.*;

@Getter
@Setter
@Builder
public class JwtRequest {
    private String username;
    private String password;
}
