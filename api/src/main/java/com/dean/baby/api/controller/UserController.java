package com.dean.baby.api.controller;

import com.dean.baby.common.dto.UpdateUserRequestVo;
import com.dean.baby.common.dto.UserDto;
import com.dean.baby.common.service.UserService;
import jdk.jfr.Description;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Description("Get user information")
    public ResponseEntity<UserDto> getUser() {
        return ResponseEntity.ok(userService.getUser());
    }

    @PatchMapping
    @Description("Update user information")
    public ResponseEntity<UserDto> update(@RequestBody UpdateUserRequestVo vo) {
        return ResponseEntity.ok(userService.update(vo));
    }
}
