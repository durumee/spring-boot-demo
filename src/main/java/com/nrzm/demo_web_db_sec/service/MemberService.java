package com.nrzm.demo_web_db_sec.service;

import com.nrzm.demo_web_db_sec.entitiy.Member;
import com.nrzm.demo_web_db_sec.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }
    public Optional<Member> findByMemberName(String memberName) {
        return memberRepository.findByMemberName(memberName);
    }

    public Member save(Member member) {
        return memberRepository.save(member);
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }
}
