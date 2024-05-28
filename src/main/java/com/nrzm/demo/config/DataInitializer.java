package com.nrzm.demo.config;

import com.nrzm.demo.entitiy.Member;
import com.nrzm.demo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Member user = new Member();
        user.setId(1L);
        user.setMemberName("user");
        user.setMemberPassword(passwordEncoder.encode("password"));
        user.setRole("ROLE_USER");
        memberRepository.save(user);

        Member admin = new Member();
        admin.setId(2L);
        admin.setMemberName("admin");
        admin.setMemberPassword(passwordEncoder.encode("admin"));
        admin.setRole("ROLE_ADMIN");
        memberRepository.save(admin);
    }
}
