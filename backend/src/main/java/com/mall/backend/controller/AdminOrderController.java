package com.mall.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.backend.common.api.Result;
import com.mall.backend.common.exception.BusinessException;
import com.mall.backend.common.jwt.UserContext;
import com.mall.backend.dto.OrderUpdateRequest;
import com.mall.backend.entity.OrderEntity;
import com.mall.backend.entity.OrderItem;
import com.mall.backend.service.OrderItemService;
import com.mall.backend.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/order")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;

    private void requireAdmin() {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "管理员权限不足");
        }
    }

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") long page,
                          @RequestParam(defaultValue = "10") long size,
                          @RequestParam(required = false) String orderNo,
                          @RequestParam(required = false) String username,
                          @RequestParam(required = false) Integer status) {
        requireAdmin();
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        if (orderNo != null && !orderNo.isBlank()) {
            wrapper.like("order_no", orderNo);
        }
        if (username != null && !username.isBlank()) {
            wrapper.like("username", username);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");
        Page<OrderEntity> pageResult = orderService.page(new Page<>(page, size), wrapper);
        return Result.success(pageResult);
    }

    @PutMapping("/confirm/{id}")
    public Result<?> confirm(@PathVariable Long id) {
        requireAdmin();
        OrderEntity order = orderService.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        order.setStatus(1);
        orderService.updateById(order);
        return Result.success(null);
    }

    @PutMapping("/complete/{id}")
    public Result<?> complete(@PathVariable Long id) {
        requireAdmin();
        OrderEntity order = orderService.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        order.setStatus(2);
        orderService.updateById(order);
        return Result.success(null);
    }

    @PutMapping("/cancel/{id}")
    public Result<?> cancel(@PathVariable Long id) {
        requireAdmin();
        OrderEntity order = orderService.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        order.setStatus(3);
        orderService.updateById(order);
        return Result.success(null);
    }

    @PutMapping("/update")
    public Result<?> update(@Valid @RequestBody OrderUpdateRequest request) {
        requireAdmin();
        OrderEntity order = orderService.getById(request.getOrderId());
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverAddress(request.getReceiverAddress());
        order.setRemark(request.getRemark());
        orderService.updateById(order);
        return Result.success(null);
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        requireAdmin();
        OrderEntity order = orderService.getById(id);
        if (order == null) {
            throw new BusinessException("订单不存在");
        }
        List<OrderItem> items = orderItemService.list(new QueryWrapper<OrderItem>().eq("order_id", id));
        return Result.success(new AdminOrderDetail(order, items));
    }

    public static class AdminOrderDetail {
        private final OrderEntity order;
        private final List<OrderItem> items;

        public AdminOrderDetail(OrderEntity order, List<OrderItem> items) {
            this.order = order;
            this.items = items;
        }

        public OrderEntity getOrder() {
            return order;
        }

        public List<OrderItem> getItems() {
            return items;
        }
    }
}
