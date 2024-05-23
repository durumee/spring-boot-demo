package com.nrzm.demo_web_db_sec.controller;

import lombok.extern.log4j.Log4j2;
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
