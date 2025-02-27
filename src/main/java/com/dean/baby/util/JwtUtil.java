package com.dean.baby.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {


    private final String secret = UUID.randomUUID().toString();

    public String generateToken(String username) {
        // token 有效期設為 1 天（單位：毫秒）
        long expiration = 86400000;
        return Jwts.builder()
                .setSubject(username) // 設置 token 主題為用戶名
                .setIssuedAt(new Date()) // 設置發行時間
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 設置過期時間
                .signWith(SignatureAlgorithm.HS512, secret) // 使用 HS512 演算法簽署 token
                .compact();
    }
}
