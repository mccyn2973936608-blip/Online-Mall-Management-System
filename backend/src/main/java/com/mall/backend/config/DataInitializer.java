package com.mall.backend.config;

import com.mall.backend.entity.*;
import com.mall.backend.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public void run(String... args) {
        try {
            initUsers();
            initCategories();
            initProducts();
        } catch (Exception e) {
            System.err.println("数据初始化失败: " + e.getMessage());
        }
    }

    private void initUsers() {
        if (userMapper.selectCount(null) > 0) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("e10adc3949ba59abbe56e057f20f883e");
        admin.setNickname("管理员");
        admin.setPhone("13800000000");
        admin.setEmail("admin@example.com");
        admin.setAddress("北京市朝阳区");
        admin.setRole(1);
        admin.setStatus(1);
        admin.setDeleted(0);
        admin.setCreateTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());
        userMapper.insert(admin);

        User member = new User();
        member.setUsername("member");
        member.setPassword("e10adc3949ba59abbe56e057f20f883e");
        member.setNickname("普通会员");
        member.setPhone("13900000000");
        member.setEmail("member@example.com");
        member.setAddress("上海市浦东新区");
        member.setRole(0);
        member.setStatus(1);
        member.setDeleted(0);
        member.setCreateTime(LocalDateTime.now());
        member.setUpdateTime(LocalDateTime.now());
        userMapper.insert(member);
    }

    private void initCategories() {
        if (categoryMapper.selectCount(null) > 0) {
            return;
        }

        Category cat1 = new Category();
        cat1.setName("手机数码");
        cat1.setDescription("热门手机与智能设备");
        cat1.setSortOrder(1);
        cat1.setDeleted(0);
        cat1.setCreateTime(LocalDateTime.now());
        cat1.setUpdateTime(LocalDateTime.now());
        categoryMapper.insert(cat1);

        Category cat2 = new Category();
        cat2.setName("家用电器");
        cat2.setDescription("居家生活电器");
        cat2.setSortOrder(2);
        cat2.setDeleted(0);
        cat2.setCreateTime(LocalDateTime.now());
        cat2.setUpdateTime(LocalDateTime.now());
        categoryMapper.insert(cat2);

        Category cat3 = new Category();
        cat3.setName("服饰鞋包");
        cat3.setDescription("时尚服饰鞋包");
        cat3.setSortOrder(3);
        cat3.setDeleted(0);
        cat3.setCreateTime(LocalDateTime.now());
        cat3.setUpdateTime(LocalDateTime.now());
        categoryMapper.insert(cat3);
    }

    private void initProducts() {
        if (productMapper.selectCount(null) > 0) {
            return;
        }

        Product p1 = new Product();
        p1.setName("旗舰智能手机");
        p1.setCategoryId(1L);
        p1.setCategoryName("手机数码");
        p1.setPrice(new BigDecimal("3999.00"));
        p1.setStock(120);
        p1.setDescription("高性能旗舰机型，畅享快速体验。");
        p1.setImageUrl("https://picsum.photos/400/400?random=1");
        p1.setStatus(1);
        p1.setDeleted(0);
        p1.setCreateTime(LocalDateTime.now());
        p1.setUpdateTime(LocalDateTime.now());
        productMapper.insert(p1);

        Product p2 = new Product();
        p2.setName("无线降噪耳机");
        p2.setCategoryId(1L);
        p2.setCategoryName("手机数码");
        p2.setPrice(new BigDecimal("899.00"));
        p2.setStock(80);
        p2.setDescription("出色降噪效果与长续航。");
        p2.setImageUrl("https://picsum.photos/400/400?random=2");
        p2.setStatus(1);
        p2.setDeleted(0);
        p2.setCreateTime(LocalDateTime.now());
        p2.setUpdateTime(LocalDateTime.now());
        productMapper.insert(p2);

        Product p3 = new Product();
        p3.setName("智能扫地机器人");
        p3.setCategoryId(2L);
        p3.setCategoryName("家用电器");
        p3.setPrice(new BigDecimal("1299.00"));
        p3.setStock(50);
        p3.setDescription("强力吸尘，智能规划路径。");
        p3.setImageUrl("https://picsum.photos/400/400?random=3");
        p3.setStatus(1);
        p3.setDeleted(0);
        p3.setCreateTime(LocalDateTime.now());
        p3.setUpdateTime(LocalDateTime.now());
        productMapper.insert(p3);

        Product p4 = new Product();
        p4.setName("男士休闲鞋");
        p4.setCategoryId(3L);
        p4.setCategoryName("服饰鞋包");
        p4.setPrice(new BigDecimal("299.00"));
        p4.setStock(150);
        p4.setDescription("舒适透气，百搭款式。");
        p4.setImageUrl("https://picsum.photos/400/400?random=4");
        p4.setStatus(1);
        p4.setDeleted(0);
        p4.setCreateTime(LocalDateTime.now());
        p4.setUpdateTime(LocalDateTime.now());
        productMapper.insert(p4);
    }
}
