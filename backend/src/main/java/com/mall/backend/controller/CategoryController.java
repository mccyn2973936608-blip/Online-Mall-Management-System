package com.mall.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.backend.common.api.Result;
import com.mall.backend.common.exception.BusinessException;
import com.mall.backend.common.jwt.UserContext;
import com.mall.backend.entity.Category;
import com.mall.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    private void requireAdmin() {
        if (!UserContext.isAdmin()) {
            throw new BusinessException(403, "管理员权限不足");
        }
    }

    @GetMapping("/list")
    public Result<?> list() {
        requireAdmin();
        List<Category> categories = categoryService.list(new QueryWrapper<Category>().orderByAsc("sort_order"));
        return Result.success(categories);
    }

    @PostMapping("/create")
    public Result<?> create(@Valid @RequestBody Category category) {
        requireAdmin();
        categoryService.save(category);
        return Result.success(null);
    }

    @PutMapping("/update")
    public Result<?> update(@Valid @RequestBody Category category) {
        requireAdmin();
        categoryService.updateById(category);
        return Result.success(null);
    }

    @DeleteMapping("/delete/{id}")
    public Result<?> delete(@PathVariable Long id) {
        requireAdmin();
        categoryService.removeById(id);
        return Result.success(null);
    }
}
