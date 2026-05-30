package com.mall.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.backend.common.api.Result;
import com.mall.backend.common.exception.BusinessException;
import com.mall.backend.common.jwt.UserContext;
import com.mall.backend.dto.OrderSubmitRequest;
import com.mall.backend.entity.CartItem;
import com.mall.backend.entity.OrderEntity;
import com.mall.backend.entity.OrderItem;
import com.mall.backend.entity.Product;
import com.mall.backend.service.CartItemService;
import com.mall.backend.service.OrderItemService;
import com.mall.backend.service.OrderService;
import com.mall.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ProductService productService;

    @PostMapping("/submit")
    public Result<?> submit(@Valid @RequestBody OrderSubmitRequest request) {
        Long userId = UserContext.getUserId();
        List<CartItem> cartItems = cartItemService.list(new QueryWrapper<CartItem>().in("id", request.getCartIds()).eq("user_id", userId));
        if (cartItems.isEmpty()) {
            throw new BusinessException("请选择要结算的商品");
        }
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Product product = productService.getById(cartItem.getProductId());
            if (product == null || product.getStatus() == null || product.getStatus() != 1) {
                throw new BusinessException("商品不存在或已下架");
            }
            if (product.getStock() < cartItem.getQuantity()) {
                throw new BusinessException("商品库存不足: " + product.getName());
            }
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            total = total.add(itemTotal);
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getImageUrl());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(itemTotal);
            orderItems.add(orderItem);
            product.setStock(product.getStock() - cartItem.getQuantity());
            productService.updateById(product);
        }
        OrderEntity order = new OrderEntity();
        order.setOrderNo("ORD" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12).toUpperCase());
        order.setUserId(userId);
        order.setUsername(UserContext.getUsername());
        order.setStatus(0);
        order.setTotalAmount(total);
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverAddress(request.getReceiverAddress());
        order.setRemark(request.getRemark());
        orderService.save(order);
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
        }
        orderItemService.saveBatch(orderItems);
        cartItemService.removeByIds(request.getCartIds());
        return Result.success(order);
    }

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") long page,
                          @RequestParam(defaultValue = "10") long size,
                          @RequestParam(required = false) Integer status) {
        Long userId = UserContext.getUserId();
        QueryWrapper<OrderEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");
        Page<OrderEntity> pageResult = orderService.page(new Page<>(page, size), wrapper);
        return Result.success(pageResult);
    }

    @PutMapping("/cancel/{id}")
    public Result<?> cancel(@PathVariable Long id) {
        Long userId = UserContext.getUserId();
        OrderEntity order = orderService.getById(id);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException("订单不存在");
        }
        if (order.getStatus() != 0) {
            throw new BusinessException("仅可取消未确认订单");
        }
        order.setStatus(3);
        orderService.updateById(order);
        return Result.success(null);
    }
}
