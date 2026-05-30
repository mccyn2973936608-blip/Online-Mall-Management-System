package com.mall.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.mall.backend.common.api.Result;
import com.mall.backend.common.exception.BusinessException;
import com.mall.backend.common.jwt.UserContext;
import com.mall.backend.dto.CartAddRequest;
import com.mall.backend.dto.CartUpdateRequest;
import com.mall.backend.entity.CartItem;
import com.mall.backend.entity.Product;
import com.mall.backend.service.CartItemService;
import com.mall.backend.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public Result<?> add(@Valid @RequestBody CartAddRequest request) {
        Long userId = UserContext.getUserId();
        Product product = productService.getById(request.getProductId());
        if (product == null || product.getStatus() == null || product.getStatus() != 1) {
            throw new BusinessException("商品不可购买");
        }
        CartItem existing = cartItemService.getOne(new QueryWrapper<CartItem>().eq("user_id", userId).eq("product_id", request.getProductId()));
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + request.getQuantity());
            cartItemService.updateById(existing);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItemService.save(cartItem);
        }
        return Result.success(null);
    }

    @GetMapping("/list")
    public Result<?> list() {
        Long userId = UserContext.getUserId();
        List<CartItem> items = cartItemService.list(new QueryWrapper<CartItem>().eq("user_id", userId));
        return Result.success(items);
    }

    @PutMapping("/update")
    public Result<?> update(@Valid @RequestBody CartUpdateRequest request) {
        Long userId = UserContext.getUserId();
        CartItem cartItem = cartItemService.getById(request.getCartId());
        if (cartItem == null || !cartItem.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在");
        }
        cartItem.setQuantity(request.getQuantity());
        cartItemService.updateById(cartItem);
        return Result.success(null);
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        Long userId = UserContext.getUserId();
        CartItem cartItem = cartItemService.getById(id);
        if (cartItem == null || !cartItem.getUserId().equals(userId)) {
            throw new BusinessException("购物车项不存在");
        }
        cartItemService.removeById(id);
        return Result.success(null);
    }
}
