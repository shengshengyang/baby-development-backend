package com.dean.baby.mvc.controller;

import com.dean.baby.mvc.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    public LoginController(LoginService loginService) {
    }

    @GetMapping("/login")
    public String login() {
        // 返回 src/main/resources/templates/login.html
        return "login";
    }

}
