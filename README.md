# 🛒 在线商城管理系统 v2.1

一个现代化的前后端分离商城管理系统，采用主流电商风格设计，支持完整的购物流程和后台管理功能。

## 🎯 项目简介

本项目是一个功能完善的在线商城系统，包含用户购物模块和管理员后台模块，采用类似淘宝/京东/拼多多的现代电商界面设计。

## ✨ 项目特色

- 🎨 **现代化电商UI** - 采用主流电商风格，橙色主色调，清晰易读
- 📱 **响应式设计** - 适配各种屏幕尺寸，手机电脑都能用
- 🔐 **权限管理** - 普通用户和管理员拥有不同的功能权限
- 💳 **完整购物流程** - 浏览商品、加入购物车、下单、支付
- 📊 **强大的后台** - 商品管理、订单管理、用户管理、分类管理
- 💾 **数据持久化** - 支持文件存储模式，重启不丢失数据

## 🛠️ 技术栈

### 后端技术
- **框架**：Spring Boot 3.x
- **ORM**：MyBatis-Plus
- **数据库**：H2（开发）/ MySQL（生产）
- **认证**：JWT Token
- **文档**：SpringDoc OpenAPI

### 前端技术
- **框架**：Vue 3 + Composition API
- **构建**：Vite
- **UI组件**：Element Plus
- **状态管理**：Pinia
- **路由**：Vue Router 4
- **HTTP**：Axios

## 📁 项目结构

```
商城/
├── backend/                      # Spring Boot 后端
│   ├── src/main/java/com/mall/backend/
│   │   ├── controller/          # 控制器层
│   │   ├── service/             # 服务层
│   │   ├── mapper/              # 数据访问层
│   │   ├── entity/               # 实体类
│   │   └── config/              # 配置类
│   └── src/main/resources/
│       └── application.yml      # 应用配置
├── frontend/                     # Vue.js 前端
│   ├── src/
│   │   ├── api/                 # API接口
│   │   ├── views/               # 页面组件
│   │   ├── components/          # 公共组件
│   │   ├── store/               # 状态管理
│   │   └── router/              # 路由配置
│   └── package.json
└── data/                         # H2数据库文件目录（运行时生成）
```

## 🚀 快速开始

### 环境要求
- JDK 17+
- Node.js 18+
- Maven 3.8+

### 方式一：使用H2数据库（推荐开发使用）

**1. 后端启动**
```bash
cd backend
mvn spring-boot:run
```
后端地址：http://localhost:8080

**2. 前端启动**
```bash
cd frontend
npm install
npm run dev
```
前端地址：http://localhost:3000

> 💡 提示：H2数据库会自动初始化测试数据，重启后数据会持久化保存到`data/mall_db.mv.db`文件。

### 方式二：使用MySQL数据库（生产环境推荐）

**1. 创建MySQL数据库**
```sql
CREATE DATABASE mall_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**2. 修改后端配置**
编辑 `backend/src/main/resources/application.yml`，修改数据库连接信息：
```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/mall_db?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root
    password: your_password
```

**3. 启动项目**
```bash
# 后端
cd backend
mvn spring-boot:run

# 前端（另一个终端）
cd frontend
npm install
npm run dev
```

## 👤 测试账号

### 管理员账号
- **用户名**：`admin`
- **密码**：`123456`
- **权限**：商品管理、用户管理、订单管理、分类管理

### 普通会员账号
- **用户名**：`member`
- **密码**：`123456`
- **权限**：浏览商品、购物车、下单、查看订单

## 📋 功能模块

### 🛍️ 用户功能

#### 1. 首页
- 商品展示（网格布局）
- 商品搜索
- 分类筛选
- 热门商品推荐

#### 2. 商品详情
- 商品图片展示
- 价格和库存显示
- 商品描述
- 加入购物车
- 立即购买

#### 3. 购物车
- 商品列表展示（图片、名称、价格）
- 数量调整（+/-按钮）
- 删除商品
- 金额统计
- 结算功能

#### 4. 订单管理
- 查看订单列表
- 订单状态追踪（待支付、待发货、已发货、已完成）
- 订单详情查看

#### 5. 用户中心
- 个人资料管理
- 密码修改
- 收货地址管理

### 🔧 管理员功能

#### 1. 管理后台首页
- 数据统计概览
- 快速操作入口
- 待处理订单提醒

#### 2. 用户管理
- 查看所有用户列表
- 编辑用户信息
- 删除用户
- 搜索用户

#### 3. 商品管理
- 商品列表展示（名称、分类、价格、库存、状态）
- 新增商品（名称、分类、价格、库存、描述、图片）
- 编辑商品信息
- 上架/下架商品
- 删除商品

#### 4. 分类管理
- 查看所有分类
- 添加新分类
- 编辑分类名称
- 调整分类排序
- 删除分类

#### 5. 订单管理
- 查看所有订单
- 订单状态管理（确认、发货、完成、取消）
- 订单搜索筛选

## 🌐 接口文档

后端启动后，可通过以下地址访问API文档：

- Swagger UI：http://localhost:8080/swagger-ui.html
- 或：http://localhost:8080/swagger-ui/index.html

## 🗄️ 数据库说明

### H2数据库控制台
- 地址：http://localhost:8080/h2-console
- JDBC URL：`jdbc:h2:file:./data/mall_db`
- 用户名：`sa`
- 密码：（留空）

### 主要数据表

#### 用户表（user）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR | 用户名 |
| password | VARCHAR | 密码（加密） |
| phone | VARCHAR | 手机号 |
| email | VARCHAR | 邮箱 |
| role | INT | 角色（0=用户，1=管理员） |

#### 商品表（product）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR | 商品名称 |
| category_id | BIGINT | 分类ID |
| category_name | VARCHAR | 分类名称 |
| price | DECIMAL | 价格 |
| stock | INT | 库存 |
| status | INT | 状态（0=下架，1=上架） |

#### 分类表（category）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| name | VARCHAR | 分类名称 |
| sort_order | INT | 排序 |

#### 订单表（order）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| order_no | VARCHAR | 订单号 |
| user_id | BIGINT | 用户ID |
| total_amount | DECIMAL | 总金额 |
| status | INT | 订单状态 |
| create_time | DATETIME | 创建时间 |

#### 购物车表（cart_item）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID |
| product_id | BIGINT | 商品ID |
| quantity | INT | 数量 |

## ❓ 常见问题

### 1. 前端无法访问后端接口
- 检查后端是否启动在8080端口
- 检查CORS配置是否正确

### 2. 数据库连接失败
- H2模式：确保`data`目录存在且有写入权限
- MySQL模式：检查数据库连接信息是否正确

### 3. 登录失败
- 检查用户名密码是否正确
- 确认数据库中是否有该用户数据

### 4. 商品分类不显示
- 确认分类表中有数据
- 检查商品是否关联了正确的分类

## 📝 版本更新

### v2.1 (当前版本)
- ✅ 修复商品分类显示问题
- ✅ 优化管理员权限控制
- ✅ 完善购物车商品信息展示

### v2.0
- ✅ 完整电商风格UI改造
- ✅ 数据库持久化支持
- ✅ 购物车功能增强

### v1.0
- ✅ 基础功能实现
- ✅ 用户注册登录
- ✅ 商品管理

## 📧 联系方式

- GitHub：https://github.com/mccyn2973936608-blip/Online-Mall-Management-System

## 📄 许可证

本项目仅供学习交流使用。
