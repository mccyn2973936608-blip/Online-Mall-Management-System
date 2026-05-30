package com.mall.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mall.backend.common.api.Result;
import com.mall.backend.common.exception.BusinessException;
import com.mall.backend.common.jwt.UserContext;
import com.mall.backend.dto.PasswordUpdateRequest;
import com.mall.backend.dto.UserUpdateRequest;
import com.mall.backend.entity.OrderEntity;
import com.mall.backend.entity.OrderItem;
import com.mall.backend.entity.User;
import com.mall.backend.service.OrderItemService;
import com.mall.backend.service.OrderService;
import com.mall.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/profile")
    public Result<?> profile() {
        Long userId = UserContext.getUserId();
        User user = userService.getById(userId);
        return Result.success(user);
    }

    @PutMapping("/profile")
    public Result<?> updateProfile(@Valid @RequestBody UserUpdateRequest request) {
        Long userId = UserContext.getUserId();
        UpdateWrapper<User> update = new UpdateWrapper<>();
        update.eq("id", userId)
                .set("nickname", request.getNickname())
                .set("phone", request.getPhone())
                .set("email", request.getEmail())
                .set("avatar", request.getAvatar())
                .set("address", request.getAddress());
        if (!userService.update(update)) {
            throw new BusinessException("更新个人信息失败");
        }
        return Result.success(null);
    }

    @PutMapping("/password")
    public Result<?> changePassword(@Valid @RequestBody PasswordUpdateRequest request) {
        Long userId = UserContext.getUserId();
        User user = userService.getById(userId);
        String oldHash = DigestUtils.md5DigestAsHex(request.getOldPassword().getBytes());
        if (!oldHash.equals(user.getPassword())) {
            throw new BusinessException("旧密码不正确");
        }
        user.setPassword(DigestUtils.md5DigestAsHex(request.getNewPassword().getBytes()));
        userService.updateById(user);
        return Result.success(null);
    }

    @GetMapping("/orders")
    public Result<?> myOrders() {
        Long userId = UserContext.getUserId();
        List<OrderEntity> orders = orderService.list(new QueryWrapper<OrderEntity>().eq("user_id", userId).orderByDesc("create_time"));
        List<OrderHistory> history = orders.stream().map(order -> {
            OrderHistory item = new OrderHistory();
            item.setOrder(order);
            item.setItems(orderItemService.list(new QueryWrapper<OrderItem>().eq("order_id", order.getId())));
            return item;
        }).toList();
        return Result.success(history);
    }

    public static class OrderHistory {
        private OrderEntity order;
        private List<OrderItem> items;

        public OrderEntity getOrder() {
            return order;
        }

        public void setOrder(OrderEntity order) {
            this.order = order;
        }

        public List<OrderItem> getItems() {
            return items;
        }

        public void setItems(List<OrderItem> items) {
            this.items = items;
        }
    }
}
