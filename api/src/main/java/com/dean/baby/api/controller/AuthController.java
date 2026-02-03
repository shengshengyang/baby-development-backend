package com.dean.baby.api.controller;

import com.dean.baby.common.dto.LoginVo;
import com.dean.baby.common.dto.RegisterVo;
import com.dean.baby.common.dto.UserDto;
import com.dean.baby.common.repository.UserRepository;
import com.dean.baby.api.service.AuthService;
import com.dean.baby.api.service.EmailService;
import com.dean.baby.api.service.RedisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "認證管理", description = "用戶註冊、登入與認證 API")
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

    @Operation(summary = "用戶註冊", description = "創建新的用戶帳號")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "註冊成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤或帳號已存在")
    })
    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "註冊資訊") @RequestBody RegisterVo vo) {
        return ResponseEntity.ok(authService.register(vo));
    }

    @Operation(summary = "用戶登入", description = "用戶登入並獲取 JWT Token")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "登入成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "帳號或密碼錯誤")
    })
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "登入資訊") @RequestBody LoginVo vo) {
        return ResponseEntity.ok(authService.login(vo));
    }

    @Operation(summary = "更新用戶資訊", description = "更新當前登入用戶的資訊")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "未授權")
    })
    @PatchMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> update(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "用戶資訊") @RequestBody RegisterVo vo) {
        return ResponseEntity.ok(authService.update(vo));
    }

    @Operation(summary = "用戶登出", description = "用戶登出系統")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "登出成功")
    })
    @PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("登出成功");
    }
}
