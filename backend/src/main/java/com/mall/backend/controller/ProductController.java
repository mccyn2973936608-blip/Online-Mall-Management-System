package com.mall.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.backend.common.api.Result;
import com.mall.backend.common.exception.BusinessException;
import com.mall.backend.common.jwt.UserContext;
import com.mall.backend.entity.Product;
import com.mall.backend.entity.Category;
import com.mall.backend.service.ProductService;
import com.mall.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") long page,
                          @RequestParam(defaultValue = "10") long size,
                          @RequestParam(required = false) Long categoryId,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(required = false) String sort) {
        QueryWrapper<Product> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        if (categoryId != null) {
            wrapper.eq("category_id", categoryId);
        }
        if (keyword != null && !keyword.isBlank()) {
            wrapper.like("name", keyword);
        }
        if ("price_asc".equals(sort)) {
            wrapper.orderByAsc("price");
        } else if ("price_desc".equals(sort)) {
            wrapper.orderByDesc("price");
        } else {
            wrapper.orderByDesc("update_time");
        }
        Page<Product> pageResult = productService.page(new Page<>(page, size), wrapper);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<?> detail(@PathVariable Long id) {
        Product product = productService.getById(id);
        if (product == null || product.getDeleted() != null && product.getDeleted() == 1) {
            throw new BusinessException("商品不存在");
        }
        return Result.success(product);
    }

    @GetMapping("/categories")
    public Result<?> categories() {
        List<Category> categories = categoryService.list(new QueryWrapper<Category>().orderByAsc("sort_order"));
        return Result.success(categories);
    }

    @PostMapping("/admin/create")
    public Result<?> create(@Valid @RequestBody Product product) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "管理员权限不足");
        }
        productService.save(product);
        return Result.success(null);
    }

    @PutMapping("/admin/update")
    public Result<?> update(@Valid @RequestBody Product product) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "管理员权限不足");
        }
        productService.updateById(product);
        return Result.success(null);
    }

    @PutMapping("/admin/status/{id}")
    public Result<?> toggleStatus(@PathVariable Long id, @RequestParam Integer status) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "管理员权限不足");
        }
        Product product = productService.getById(id);
        if (product == null) {
            throw new BusinessException("商品不存在");
        }
        product.setStatus(status);
        productService.updateById(product);
        return Result.success(null);
    }

    @DeleteMapping("/admin/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "管理员权限不足");
        }
        productService.removeById(id);
        return Result.success(null);
    }
}
