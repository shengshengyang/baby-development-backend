package com.dean.baby.api.service;

import com.dean.baby.common.dto.BabyDto;
import com.dean.baby.common.dto.LoginVo;
import com.dean.baby.common.dto.RegisterVo;
import com.dean.baby.common.dto.UserDto;
import com.dean.baby.common.entity.Role;
import com.dean.baby.common.entity.User;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.RoleRepository;
import com.dean.baby.common.repository.UserRepository;
import com.dean.baby.common.service.BaseService;
import com.dean.baby.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthService extends BaseService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    protected AuthService(UserRepository userRepository,
                          RoleRepository roleRepository,
                          AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getCurrentUser() {
        return super.getCurrentUser();
    }

    public UserDto login(LoginVo loginRequest) {

        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(
                () -> new ApiException(SysCode.USER_NOT_FOUND, "User not found")
        );

        // 確保用戶有角色，如果沒有則自動分配 ROLE_USER
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            assignDefaultRole(user);
            userRepository.save(user);
            logger.info("Assigned default role ROLE_USER to user: {}", user.getUsername());
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), loginRequest.password()));
        } catch (Exception e) {
            throw new ApiException(SysCode.AUTHENTICATION_FAILED, "Authentication failed for user: " + user.getUsername());
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String jwt = jwtUtil.generateToken(userDetails.getUsername());

        UserDto userDto = UserDto.builder()
                .username(userDetails.getUsername())
                .token(jwt)
                .email(user.getEmail())
                .babies(user.getBabies() != null ? user.getBabies().stream().map(BabyDto::fromEntity).toList() : List.of())
                .role(user.getRoles().stream()
                        .map(Role::getName)
                        .toList())
                .build();

        logger.info("Login successful for user: {}", user.getUsername());

        return userDto;
    }

    @Transactional
    public UserDto register(RegisterVo vo) {
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
        return login(LoginVo.builder().email(vo.email()).password(vo.password()).build());
    }

    public UserDto update(RegisterVo vo) {
        User user = getCurrentUser();
        user.setEmail(vo.email());
        user.setUsername(vo.username());
        user.setPassword(passwordEncoder.encode(vo.password()));
        userRepository.save(user);
        return toDto(user);
    }

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

    private UserDto toDto(User user) {
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
                .build();
    }

    @Transactional
    protected void assignDefaultRole(User user) {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ApiException(SysCode.SYSTEM_ERROR, "Default role ROLE_USER not found"));

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
