package com.nrzm.demo_web_db_sec.controller;

import com.nrzm.demo_web_db_sec.entitiy.Member;
import com.nrzm.demo_web_db_sec.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class MemberController {

    @Autowired
    MemberService memberService;

    @GetMapping("/members")
    public String members(Model model) {
        List<Member> members = memberService.findAll();
        model.addAttribute("members", members);
        return "member/memberList";
    }
}
