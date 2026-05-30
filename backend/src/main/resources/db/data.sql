INSERT INTO "user" (username, password, nickname, phone, email, address, role, status) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '管理员', '13800000000', 'admin@example.com', '北京市朝阳区', 1, 1),
('member', 'e10adc3949ba59abbe56e057f20f883e', '普通会员', '13900000000', 'member@example.com', '上海市浦东新区', 0, 1);

INSERT INTO category (name, description, sort_order) VALUES
('手机数码', '热门手机与智能设备', 1),
('家用电器', '居家生活电器', 2),
('服饰鞋包', '时尚服饰鞋包', 3);

INSERT INTO product (name, category_id, category_name, price, stock, description, image_url, status) VALUES
('旗舰智能手机', 1, '手机数码', 3999.00, 120, '高性能旗舰机型，畅享快速体验。', 'https://via.placeholder.com/400', 1),
('无线降噪耳机', 1, '手机数码', 899.00, 80, '出色降噪效果与长续航。', 'https://via.placeholder.com/400', 1),
('智能扫地机器人', 2, '家用电器', 1299.00, 50, '强力吸尘，智能规划路径。', 'https://via.placeholder.com/400', 1),
('男士休闲鞋', 3, '服饰鞋包', 299.00, 150, '舒适透气，百搭款式。', 'https://via.placeholder.com/400', 1);
