package com.dean.baby.common.service;

import com.dean.baby.common.dto.LoginVo;
import com.dean.baby.common.dto.RegisterVo;
import com.dean.baby.common.dto.UpdateUserRequestVo;
import com.dean.baby.common.dto.UserDto;
import com.dean.baby.common.entity.User;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService extends BaseService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    protected UserService(UserRepository userRepository, UserRepository userRepository1, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userRepository = userRepository1;
        this.passwordEncoder = passwordEncoder;
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
        return UserDto.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .role(List.of("ROLE_USER"))
                .build();
    }
}
