package com.nrzm.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class LoginController {

    @GetMapping("/loginPage")
    public String loginPage() {
        log.info("로그인페이지");
        return "loginPage"; // loginPage.html 파일을 반환
    }
}
