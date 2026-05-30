# 商城管理系统（前后端分离）

## 项目目录

- `backend`：Spring Boot 后端项目
- `frontend`：Vue 3 + Vite 前端项目
- `sql/mall_schema.sql`：数据库建表脚本
- `sql/mall_data.sql`：初始测试数据

## 技术栈

- 后端：Spring Boot 3 + MyBatis-Plus + MySQL 8 + Maven
- 前端：Vue 3 + Vite + Element Plus + Vue Router + Pinia
- 接口文档：SpringDoc OpenAPI

## 运行前准备

1. 安装 MySQL 8
2. 创建数据库并导入脚本：
   - 执行 `sql/mall_schema.sql`
   - 执行 `sql/mall_data.sql`
3. 修改后端数据库配置（如有必要）：
   - `backend/src/main/resources/application.yml`
   - 默认连接：`jdbc:mysql://127.0.0.1:3306/mall_db`
   - 默认用户：`root`，默认密码：`root`

## 后端启动

1. 打开终端，进入 `backend` 目录
2. 执行：
   ```bash
   mvn spring-boot:run
   ```
3. 访问接口文档：
   - `http://127.0.0.1:8080/swagger-ui.html`
   - 或 `http://127.0.0.1:8080/swagger-ui/index.html`

## 前端启动

1. 打开终端，进入 `frontend` 目录
2. 执行：
   ```bash
   npm install
   npm run dev
   ```
3. 默认访问：
   - `http://127.0.0.1:3000`

## 测试账号

- 管理员：
  - 用户名：`admin`
  - 密码：`123456`
- 普通会员：
  - 用户名：`member`
  - 密码：`123456`

## 功能说明

- 会员：注册、登录、个人资料维护、密码修改、查看订单
- 管理员：会员管理、商品分类管理、商品管理、订单管理
- 购物流程：浏览商品、加入购物车、编辑购物车、提交订单、查看订单

## 注意事项

- 后端接口统一返回格式：`code`、`message`、`data`
- 前后端已配置跨域
- 敏感操作需确认弹窗
- 前端和后端均已开启参数校验
