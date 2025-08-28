package com.dean.baby.common.service;

import com.dean.baby.common.dto.*;
import com.dean.baby.common.entity.Role;
import com.dean.baby.common.entity.User;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.BabyRepository;
import com.dean.baby.common.repository.RoleRepository;
import com.dean.baby.common.repository.UserRepository;
import com.dean.baby.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;
import java.util.List;

@Service
public class UserService extends BaseService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BabyRepository babyRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;

    protected UserService(UserRepository userRepository, UserRepository userRepository1, PasswordEncoder passwordEncoder, BabyRepository babyRepository, RoleRepository roleRepository, JwtUtil jwtUtil) {
        super(userRepository);
        this.userRepository = userRepository1;
        this.passwordEncoder = passwordEncoder;
        this.babyRepository = babyRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public UserDto getUser() {
        User user = getCurrentUser();
        // 確保用戶有角色
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            assignDefaultRole(user);
            userRepository.save(user);
            logger.info("Assigned default role ROLE_USER to user: {}", user.getUsername());
        }
        return toDto(user);
    }

    @Transactional
    public UserDto update(UpdateUserRequestVo vo) {
        User user = getCurrentUser();
        user.setEmail(vo.email());
        user.setPassword(passwordEncoder.encode(vo.password()));
        // 確保用戶有角色
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            assignDefaultRole(user);
            logger.info("Assigned default role ROLE_USER to user: {}", user.getUsername());
        }
        return toDto(userRepository.save(user));
    }

    @Transactional
    public void register(RegisterVo vo) {
        userRepository.findByEmail(vo.email()).ifPresent(user -> {
            throw new ApiException(SysCode.USER_ALREADY_EXISTS, "用戶已存在");
        });
        User user = User.builder()
                .email(vo.email())
                .username(vo.username())
                .password(passwordEncoder.encode(vo.password()))
                .build();

        // 自動分配 ROLE_USER 角色
        assignDefaultRole(user);
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
                .role(user.getRoles().stream()
                        .map(Role::getName)
                        .toList())
                .token(token)
                .babies(babies)
                .build();
    }

    @Transactional
    protected void assignDefaultRole(User user) {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ApiException(SysCode.ROLE_NOT_FOUND, "Default role ROLE_USER not found"));

        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(userRole);
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
