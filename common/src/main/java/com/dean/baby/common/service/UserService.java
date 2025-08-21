package com.dean.baby.common.service;

import com.dean.baby.common.dto.*;
import com.dean.baby.common.entity.User;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.BabyRepository;
import com.dean.baby.common.repository.UserRepository;
import com.dean.baby.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

@Service
public class UserService extends BaseService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BabyRepository babyRepository;
    private final JwtUtil jwtUtil;

    protected UserService(UserRepository userRepository, UserRepository userRepository1, PasswordEncoder passwordEncoder, BabyRepository babyRepository, JwtUtil jwtUtil) {
        super(userRepository);
        this.userRepository = userRepository1;
        this.passwordEncoder = passwordEncoder;
        this.babyRepository = babyRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional(readOnly = true)
    public UserDto getUser() {
        return toDto(getCurrentUser());
    }

    @Transactional
    public UserDto update(UpdateUserRequestVo vo) {
        User user = getCurrentUser();
        user.setEmail(vo.email());
        user.setPassword(passwordEncoder.encode(vo.password()));
        return toDto(userRepository.save(user));
    }

    public void register(RegisterVo vo) {
        userRepository.findByEmail(vo.email()).ifPresent(user -> {
            throw new ApiException(SysCode.USER_ALREADY_EXISTS, "用戶已存在");
        });
        User user = User.builder()
                .email(vo.email())
                .username(vo.username())
                .password(passwordEncoder.encode(vo.password()))
                .build();
        userRepository.save(user);
    }


    private UserDto toDto(User user) {
        List<BabyDto> babies = babyRepository.findByUserId(user.getId()).stream()
                .map(BabyDto::fromEntity).toList();

        String token = getCurrentToken();
        if (token == null || !jwtUtil.validateToken(token, user.getEmail())) {
            token = jwtUtil.generateToken(user.getEmail());
        }
        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(List.of("ROLE_USER"))
                .token(token)
                .babies(babies)
                .build();
    }

    private String getCurrentToken() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String authHeader = request.getHeader("Authorization");
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    return authHeader.substring(7);
                }
            }
        } catch (Exception e) {
            logger.debug("Failed to get current token from request", e);
        }
        return null;
    }
}
