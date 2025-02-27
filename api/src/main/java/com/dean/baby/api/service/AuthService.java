package com.dean.baby.api.service;

import com.dean.baby.common.dto.LoginVo;
import com.dean.baby.common.dto.RegisterVo;
import com.dean.baby.common.dto.UserDto;
import com.dean.baby.common.entity.User;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.UserRepository;
import com.dean.baby.api.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthService extends BaseService{

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    protected AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil, UserRepository userRepository1, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository1;
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
                .role(userDetails.getAuthorities().stream()
                        .map(Object::toString)
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

    private UserDto toDto(User user) {
        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(List.of("ROLE_USER"))
                .build();
    }
}
