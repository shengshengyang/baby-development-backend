package com.dean.baby.mvc.service;

import com.dean.baby.common.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;


    public LoginService(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
    }
}
