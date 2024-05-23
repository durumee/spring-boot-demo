package com.nrzm.demo_web_db_sec.security;

import com.nrzm.demo_web_db_sec.entitiy.Member;
import com.nrzm.demo_web_db_sec.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberName(username);
        if (member == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new User(member.getMemberName(), member.getMemberPassword(), Collections.singletonList(new SimpleGrantedAuthority(member.getRole())));
    }
}
