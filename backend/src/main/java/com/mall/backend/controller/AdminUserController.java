package com.mall.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.backend.common.api.Result;
import com.mall.backend.common.exception.BusinessException;
import com.mall.backend.common.jwt.UserContext;
import com.mall.backend.entity.OrderEntity;
import com.mall.backend.entity.User;
import com.mall.backend.service.OrderService;
import com.mall.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
public class AdminUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    private void requireAdmin() {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "管理员权限不足");
        }
    }

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") long page,
                          @RequestParam(defaultValue = "10") long size,
                          @RequestParam(required = false) String keyword) {
        requireAdmin();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.ne("role", -1);
        if (keyword != null && !keyword.isBlank()) {
            wrapper.and(w -> w.like("username", keyword).or().like("phone", keyword));
        }
        wrapper.orderByDesc("create_time");
        Page<User> pageResult = userService.page(new Page<>(page, size), wrapper);
        return Result.success(pageResult);
    }

    @PostMapping("/create")
    public Result<?> create(@Valid @RequestBody User user) {
        requireAdmin();
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername()).or().eq("phone", user.getPhone()).or().eq("email", user.getEmail());
        if (userService.count(wrapper) > 0) {
            throw new BusinessException("用户名、手机号或邮箱已被使用");
        }
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        user.setRole(user.getRole() == null ? 0 : user.getRole());
        userService.save(user);
        return Result.success(null);
    }

    @PutMapping("/update")
    public Result<?> update(@Valid @RequestBody User user) {
        requireAdmin();
        userService.updateById(user);
        return Result.success(null);
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        requireAdmin();
        userService.removeById(id);
        return Result.success(null);
    }

    @GetMapping("/{id}/orders")
    public Result<?> getUserOrders(@PathVariable Long id) {
        requireAdmin();
        List<OrderEntity> list = orderService.list(new QueryWrapper<OrderEntity>().eq("user_id", id).orderByDesc("create_time"));
        return Result.success(list);
    }
}
