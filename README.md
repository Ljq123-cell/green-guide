# GreenGuide 智能垃圾分类AI教育科普助手

> 大模型驱动的智能垃圾分类AI教育科普助手 — 后端服务

## 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 框架 | Spring Boot | 3.2.5 |
| ORM | MyBatis-Plus | 3.5.5 |
| 数据库 | MySQL | 8.0 |
| 连接池 | Druid | 1.2.20 |
| 认证 | Spring Security + JWT (jjwt) | 0.12.5 |
| AI | DeepSeek API | deepseek-chat |
| 工具 | Lombok / Hutool | — |
| JDK | Java 17 | — |

## 项目结构

```
src/main/java/com/greenguide/
├── GreenGuideApplication.java          # 启动入口
├── common/                              # 通用层
│   ├── Result.java                      # 统一响应体
│   ├── PageResult.java                  # 分页响应
│   ├── BaseEntity.java                  # 实体基类（时间戳/逻辑删除）
│   └── exception/                       # 全局异常处理
├── config/                              # 配置
│   ├── SecurityConfig.java              # Spring Security
│   ├── CorsConfig.java                  # 跨域
│   └── MyBatisPlusConfig.java           # 分页插件 + 自动填充
├── security/                            # JWT认证
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   ├── AdminUserDetails / AdminUserDetailsService.java
│   └── UserPrincipal.java
├── dto/                                 # 传输对象
├── module/
│   ├── admin/                           # 管理员（登录/用户管理）
│   ├── user/                            # 普通用户（登录/个人信息）
│   ├── auth/                            # 统一登录
│   ├── ai/                              # DeepSeek AI 服务
│   ├── garbage/                         # 垃圾分类数据
│   ├── knowledge/                       # 科普文章
│   ├── quiz/                            # 题库 + 答题记录 + 排行榜
│   ├── learning/                        # 学习记录 + 积分
│   ├── feedback/                        # 用户反馈
│   └── achievement/                     # 成就徽章
└── resources/
    ├── application.yml                  # 主配置
    ├── application-dev.yml              # 开发环境（数据库/Redis）
    ├── db/init.sql                      # 数据库初始化脚本
    ├── db/extra_quiz.sql                # 额外题目数据
    ├── db/extra_article.sql             # 科普文章数据
    ├── db/extra_user.sql                # 示例用户数据
    └── static/                          # 前端页面
```

## 快速启动

### 1. 环境要求

- JDK 17
- MySQL 8.0
- Maven 3.9+

### 2. 创建数据库

```bash
mysql -u root -p < src/main/resources/db/init.sql
mysql -u root -p < src/main/resources/db/extra_quiz.sql
mysql -u root -p < src/main/resources/db/extra_article.sql
mysql -u root -p < src/main/resources/db/extra_user.sql
```

### 3. 配置

编辑 `src/main/resources/application-dev.yml`，修改数据库连接：

```yaml
spring:
  datasource:
    druid:
      username: root
      password: 你的密码
```

DeepSeek API Key 在 `application.yml` 的 `deepseek.api-key` 中配置。

### 4. 启动

```bash
mvn spring-boot:run
```

服务运行在 `http://localhost:8686`

## 页面入口

| 页面 | 地址 | 说明 |
|------|------|------|
| 统一登录 | `/login.html` | 管理员/普通用户统一登录 |
| 用户端 | `/index.html` | AI分类 + 科普 + 答题 + 排行榜 + 成就 |
| 管理后台 | `/admin.html` | 文章/题库/反馈管理 |

## 测试账号

| 角色 | 账号 | 密码 |
|------|------|------|
| 管理员 | admin | admin123 |
| 普通用户 | 环保小明 | user123 |
| 普通用户 | user | 123 |
| 普通用户 | 环保小卫士 | test |
| 普通用户 | 垃圾分类达人 | test |
| 普通用户 | 环保先锋 | test |
| 普通用户 | 爱回收的小明 | test |
| 普通用户 | 地球守护者 | test |
| ...（共10名示例用户，密码均为 test） | | |

## API 清单

### 公开接口（无需认证）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/v1/public/unified-login` | 统一登录 |
| GET | `/api/v1/public/garbage/search?keyword=` | 搜索垃圾分类 |
| GET | `/api/v1/public/garbage/all` | 全部分类 |
| GET | `/api/v1/public/garbage?category=` | 按类别筛选 |
| GET | `/api/v1/public/garbage/{id}` | 分类详情 |
| POST | `/api/v1/public/ai/classify` | AI智能分类问答 |
| POST | `/api/v1/public/ai/generate-quiz` | AI生成题目 |
| GET | `/api/v1/public/knowledge` | 科普文章列表 |
| GET | `/api/v1/public/knowledge/{id}` | 文章详情 |
| GET | `/api/v1/public/quiz/random?count=10` | 随机出题 |
| POST | `/api/v1/public/quiz/submit` | 提交答案 |
| GET | `/api/v1/public/quiz/stats/{userId}` | 答题统计 |
| GET | `/api/v1/public/leaderboard` | 排行榜 |
| POST | `/api/v1/public/feedback` | 提交反馈 |
| GET | `/api/v1/achievements` | 成就列表 |

### 管理接口（需 ADMIN/EDITOR Token）

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/v1/admin/login` | 管理员登录 |
| GET/POST/PUT/DELETE | `/api/v1/admin/knowledge[/{id}]` | 文章CRUD |
| PUT | `/api/v1/admin/knowledge/{id}/publish` | 发布文章 |
| PUT | `/api/v1/admin/knowledge/{id}/unpublish` | 下架文章 |
| GET/POST/PUT/DELETE | `/api/v1/admin/quiz[/{id}]` | 题目CRUD |
| PUT | `/api/v1/admin/quiz/{id}/publish` | 发布题目 |
| GET | `/api/v1/admin/feedback` | 反馈列表 |
| PUT | `/api/v1/admin/feedback/{id}/process` | 处理反馈 |

## 演示数据

- 32 条垃圾分类数据（可回收物9条、有害垃圾6条、厨余垃圾6条、其他垃圾11条）
- 10 篇科普文章（涵盖四大类别：可回收物、有害垃圾、厨余垃圾、其他垃圾）
- 10 名示例用户（微信小程序端）
- 15 道选择题（入门/进阶/挑战三个难度）
- 5 个成就徽章
