package com.dean.baby.mvc.controller;

import com.dean.baby.common.dto.RegisterVo;
import com.dean.baby.common.service.UserService;
import com.dean.baby.common.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@Slf4j
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterVo vo, Model model, RedirectAttributes redirectAttributes) {
        try {
            userService.register(vo);
            redirectAttributes.addFlashAttribute("success", "註冊成功，請使用新帳號登入");
            return "redirect:/login";
        } catch (ApiException ex) {
            model.addAttribute("error", ex.getMessage());
            return "register";
        } catch (Exception ex) {
            model.addAttribute("error", "註冊失敗，請稍後再試");
            return "register";
        }
    }
}
