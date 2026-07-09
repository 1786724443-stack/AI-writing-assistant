# AI 写作助手 - Spring Boot 后端服务实现计划

## 1. 项目定位

纯调用大模型 API 的 Spring Boot 后端服务，不部署任何模型。提供文章生成、润色、扩写、摘要、翻译等能力。

## 2. 核心功能

- **文本生成**: 输入主题/大纲，生成文章
- **文本润色**: 优化语法、风格
- **扩写/缩写**: 调整内容长度
- **摘要提取**: 提取关键信息
- **多语种翻译**: 支持多语言互译
- **用户调用配额与历史记录**: 强化工程能力

## 3. 技术栈

| 分类 | 技术 | 版本 | 说明 |
|------|------|------|------|
| 语言 | Java | 17+ | LTS 版本 |
| 框架 | Spring Boot | 3.x | 最新稳定版本 |
| 数据库 | H2（开发）/ MySQL（生产） | - | Spring Data JPA |
| HTTP 客户端 | RestTemplate / WebClient | - | 用于调用大模型 API |
| 缓存（可选） | Caffeine | - | 本地缓存 |
| API 文档 | SpringDoc OpenAPI | 2.3+ | Swagger UI |
| 流式响应 | Server-Sent Events (SSE) | - | 实时返回生成结果 |
| API 供应商 | OpenAI / 百度文心 / 阿里通义 / DeepSeek | - | 任选其一或多个 |

## 4. 阶段划分

### 阶段一：API 打通 & 基础骨架

**目标**: 能用 Postman 调用自己的接口生成一篇文章

**任务清单**:
1. 创建 Spring Boot 项目，引入 Web、Lombok 依赖
2. 封装统一的 AI 调用工具类 AiClient（支持更换 Provider）
3. 设计通用的请求/响应 DTO
4. 编写第一个接口 POST /api/generate，接收 topic 和 style
5. 处理 API 异常、超时、重试

### 阶段二：多能力扩展 & Prompt 工程

**目标**: 完成润色、扩写、摘要、翻译接口，重点打磨 Prompt

**任务清单**:
1. 设计 Prompt 模板管理（枚举/配置文件），为每种能力定义 System/User Prompt
2. 复用 AiClient 实现：
   - POST /api/polish
   - POST /api/expand
   - POST /api/summarize
   - POST /api/translate
3. 增加请求参数校验（文本长度、语言代码等）
4. 测试对比不同 Prompt 对结果的影响

### 阶段三：用户体系与调用控制

**目标**: 让项目从工具变成可运营的服务

**任务清单**:
1. 设计用户表、调用记录表，JPA 映射
2. 实现注册/登录（简单 Token 或 JWT）
3. 每次调用前校验配额，调用后扣减并记录
4. 提供历史记录查询接口 GET /api/history
5. 查询剩余配额 GET /api/quota
6. 对超配额请求返回 429 友好提示

### 阶段四：体验升级与交付

**目标**: 优化性能、增加前端、打包部署，形成完整项目

**任务清单**:
1. 引入 @Async 异步化 AI 调用，避免阻塞
2. 尝试支持 SSE 流式输出（如果所用 API 支持）
3. 使用 Thymeleaf 写一个极简前端页面（或继续用 Swagger）
4. 集成 Swagger 文档，生成接口说明
5. 用 JMeter 压测配额限流效果
6. 编写 Dockerfile，生成镜像，确保一键启动
7. 撰写 GitHub README，包括架构图、启动指南

## 5. 模块结构

```
ai-writing-assistant/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/aiwriter/
│       │       ├── controller/          # REST API 控制层
│       │       ├── service/             # 业务逻辑层
│       │       ├── client/              # AI API 客户端层
│       │       ├── prompt/              # 提示词管理
│       │       ├── entity/              # 数据库实体
│       │       ├── dto/                 # 数据传输对象
│       │       ├── config/              # 配置类
│       │       ├── exception/           # 异常处理
│       │       └── AiWriterApplication.java
│       └── resources/
│           ├── application.yml          # 应用配置
│           └── prompts/                 # 提示词模板文件
└── pom.xml
```

## 6. API 设计

### 6.1 文章生成

```
POST /api/generate
Content-Type: application/json

{
  "topic": "人工智能的发展趋势",
  "style": "formal"
}

Response:
{
  "id": "uuid",
  "content": "...生成的文章内容...",
  "tokenCount": 1200,
  "duration": 3500
}
```

### 6.2 文章润色

```
POST /api/polish
Content-Type: application/json

{
  "content": "原始文章内容...",
  "style": "formal"
}
```

### 6.3 文章扩写

```
POST /api/expand
Content-Type: application/json

{
  "content": "需要扩写的内容...",
  "targetLength": 2000
}
```

### 6.4 文章摘要

```
POST /api/summarize
Content-Type: application/json

{
  "content": "需要摘要的文章...",
  "maxLength": 300
}
```

### 6.5 文章翻译

```
POST /api/translate
Content-Type: application/json

{
  "content": "需要翻译的内容...",
  "targetLanguage": "zh-CN"
}
```

### 6.6 历史记录查询

```
GET /api/history?page=0&size=10

Response:
{
  "content": [...],
  "totalElements": 100,
  "totalPages": 10
}
```

### 6.7 配额查询

```
GET /api/quota

Response:
{
  "totalQuota": 1000,
  "usedQuota": 150,
  "remainingQuota": 850
}
```

## 7. 数据模型

### 7.1 用户表 (`users`)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | UUID | 主键 |
| username | String | 用户名（唯一） |
| password | String | 密码（加密） |
| email | String | 邮箱 |
| total_quota | INT | 总配额 |
| used_quota | INT | 已用配额 |
| created_at | TIMESTAMP | 创建时间 |
| updated_at | TIMESTAMP | 更新时间 |

### 7.2 调用记录表 (`api_call`)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | UUID | 主键 |
| user_id | UUID | 用户ID |
| operation_type | String | 操作类型 |
| input_content | TEXT | 输入内容 |
| output_content | TEXT | 输出内容 |
| token_usage | INT | Token 使用量 |
| provider | String | 使用的 AI 提供商 |
| duration_ms | INT | 耗时（毫秒） |
| created_at | TIMESTAMP | 创建时间 |

## 8. 风险与注意事项

- **API 调用失败**: 配置重试机制和熔断策略
- **API 密钥安全**: 使用环境变量注入，禁止硬编码
- **成本控制**: 实时监控 Token 使用量，设置阈值告警
- **输入验证**: 对用户输入进行严格校验
- **日志安全**: 不要输出任何可复原密钥的片段
- **文件编码**: Windows 环境下统一使用 UTF-8 无 BOM
