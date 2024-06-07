package com.nrzm.demo.config.security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserDetailsConfig {

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        return new InMemoryUserDetailsManager();
    }

    @Bean
    public CommandLineRunner initUsers(InMemoryUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
        return args -> {
            UserDetails user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("1234"))
                    .roles("USER")
                    .build();

            UserDetails admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("1234"))
                    .roles("ADMIN")
                    .build();

            userDetailsManager.createUser(user);
            userDetailsManager.createUser(admin);
        };
    }
}
