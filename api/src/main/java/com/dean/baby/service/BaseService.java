package com.dean.baby.common.service;

import com.dean.baby.common.entity.User;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public abstract class BaseService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;

    protected BaseService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ApiException(SysCode.NOT_LOGIN);
        }
        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new ApiException(SysCode.USER_NOT_FOUND, authentication.getName()));
    }

    protected void isCurrentUserBabyOwner(UUID babyId) {
        if(getCurrentUser().getBabies().stream()
                .noneMatch(baby -> baby.getId().equals(babyId))){
            throw new  ApiException(SysCode.NOT_YOUR_BABY);
        }
    }
}
