package com.dean.baby.service;

import com.dean.baby.dto.LoginVo;
import com.dean.baby.dto.UserDto;
import com.dean.baby.entity.User;
import com.dean.baby.exception.ApiException;
import com.dean.baby.exception.SysCode;
import com.dean.baby.repository.UserRepository;
import com.dean.baby.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends BaseService{

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    protected AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil, UserRepository userRepository1) {
        super(userRepository);
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository1;
    }

    @Override
    public User getCurrentUser() {
        return super.getCurrentUser();
    }

    public UserDto login(LoginVo loginRequest) {

        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(
                () -> new ApiException(SysCode.USER_NOT_FOUND, "User not found")
        );

        //verify password


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
}
