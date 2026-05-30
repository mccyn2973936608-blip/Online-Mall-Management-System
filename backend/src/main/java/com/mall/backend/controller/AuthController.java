package com.mall.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.backend.common.api.Result;
import com.mall.backend.common.exception.BusinessException;
import com.mall.backend.common.jwt.JwtUtil;
import com.mall.backend.dto.LoginRequest;
import com.mall.backend.dto.RegisterRequest;
import com.mall.backend.entity.User;
import com.mall.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<?> register(@Valid @RequestBody RegisterRequest request) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", request.getUsername()).or().eq("phone", request.getPhone()).or().eq("email", request.getEmail());
        if (userService.count(wrapper) > 0) {
            throw new BusinessException("用户名、手机号或邮箱已被使用");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes()));
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setNickname(request.getUsername());
        user.setRole(0);
        user.setStatus(1);
        userService.save(user);
        return Result.success("注册成功", null);
    }

    @PostMapping("/login")
    public Result<?> login(@Valid @RequestBody LoginRequest request) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", request.getUsername());
        User user = userService.getOne(wrapper);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        String password = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
        if (!password.equals(user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        String token = JwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole(), request.isRememberMe());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("user", user);
        return Result.success(data);
    }

    @GetMapping("/info")
    public Result<?> getUserInfo() {
        Long userId = com.mall.backend.common.jwt.UserContext.getUserId();
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return Result.success(user);
    }
}
