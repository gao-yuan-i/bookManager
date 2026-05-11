package com.example.bookmanager.controller;

import com.example.bookmanager.common.Result;
import com.example.bookmanager.dto.LoginDTO;
import com.example.bookmanager.dto.RegisterDTO;
import com.example.bookmanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return Result.success("注册成功");
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO) {
        String token = userService.login(loginDTO);

        // 从token中解析角色，返回给前端用于路由跳转
        // 简化处理：直接在service返回前获取，这里让前端解析token也行
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        return Result.success("登录成功", data);
    }
}