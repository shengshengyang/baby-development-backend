package com.dean.baby.api.controller;

import com.dean.baby.common.dto.LoginVo;
import com.dean.baby.common.dto.RegisterVo;
import com.dean.baby.common.dto.UserDto;
import com.dean.baby.common.entity.User;
import com.dean.baby.common.repository.UserRepository;
import com.dean.baby.api.service.AuthService;
import com.dean.baby.api.service.EmailService;
import com.dean.baby.api.service.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final EmailService emailService;
    private final AuthService authService;
    Random random = new Random();

    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder, RedisService redisService, EmailService emailService, AuthService authService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
        this.emailService = emailService;
        this.authService = authService;
    }


    @PostMapping("/register")
    public String register(@NonNull @RequestParam String email) {

        String code = String.valueOf(random.nextInt(900000) + 100000); // 6 位驗證碼
        redisService.saveVerificationCode(email, code);
        emailService.sendVerificationCode(email, code);
        return "驗證碼已發送至 " + email;
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@NonNull @RequestParam String email,@NonNull @RequestParam String code, @RequestBody User user) {
        if (redisService.verifyCode(email, code)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepo.save(user);
            redisService.deleteVerificationCode(email);
            return ResponseEntity.ok("Verification successful");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification code not match or expired");
    }

    @PostMapping("/create")
    public ResponseEntity<UserDto> create(@RequestBody RegisterVo vo) {
        return ResponseEntity.ok(authService.register(vo));
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginVo vo) {
        return ResponseEntity.ok(authService.login(vo));
    }

    @PatchMapping
    public ResponseEntity<UserDto> update(@RequestBody RegisterVo vo) {
        return ResponseEntity.ok(authService.update(vo));
    }

    @PostMapping("/logout")
    public String logout() {
        return "登出成功";
    }
}
