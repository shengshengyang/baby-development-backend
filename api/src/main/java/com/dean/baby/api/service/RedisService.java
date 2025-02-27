package com.dean.baby.api.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveVerificationCode(String email, String code) {
        redisTemplate.opsForValue().set(email, code, 5, TimeUnit.MINUTES);
    }

    public boolean verifyCode(String email, String code) {
        String storedCode = redisTemplate.opsForValue().get(email);
        return storedCode != null && storedCode.equals(code);
    }

    public void deleteVerificationCode(String email) {
        redisTemplate.delete(email);
    }
}
