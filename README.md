# AI Writing Assistant

一个基于 Spring Boot 的 AI 写作助手后端服务，提供文章生成、润色、扩写、摘要、翻译等能力。

## 功能特性

- **文章生成**：输入主题/大纲，自动生成文章
- **文本润色**：优化语法、风格，提升文章质量
- **内容扩写**：扩展内容长度，丰富细节
- **摘要提取**：提取文本关键信息
- **多语种翻译**：支持中、英、日、韩、法、德等语言
- **用户认证**：基于 JWT 的登录/注册系统
- **配额管理**：每日调用配额限制与自动重置
- **历史记录**：查看调用历史与结果详情

## 技术栈

- **语言**：Java 17+
- **框架**：Spring Boot 3.x
- **数据库**：H2（开发）/ MySQL（生产）
- **ORM**：Spring Data JPA
- **HTTP 客户端**：RestTemplate
- **认证**：JWT + BCrypt
- **API 文档**：SpringDoc OpenAPI (Swagger)
- **AI 提供商**：DeepSeek / OpenAI（可配置）

## 快速开始

### 环境要求

- JDK 17 或更高版本
- Maven 3.8+

### 配置 API Key

在 `src/main/resources/application.yml` 中配置 AI 提供商的 API Key：

```yaml
ai:
  type: deepseek
  deepseek:
    api-key: your-deepseek-api-key
```

或者通过环境变量配置：

```bash
# Windows
set DEEPSEEK_API_KEY=your-deepseek-api-key

# Linux/macOS
export DEEPSEEK_API_KEY=your-deepseek-api-key
```

### 运行项目

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

服务启动后访问：

- **UI 页面**：http://localhost:8080/
- **Swagger 文档**：http://localhost:8080/swagger-ui.html
- **H2 控制台**：http://localhost:8080/h2-console/

### 默认账户

应用启动时会自动创建默认账户（需启用 `app.default-user.enabled=true`）：

| 项目 | 值 |
|------|-----|
| 用户名 | `admin` |
| 密码 | `admin123` |
| 每日配额 | 100 次 |

## API 接口

### 认证接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/auth/register` | POST | 用户注册 |
| `/api/auth/login` | POST | 用户登录 |

### 写作接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/generate` | POST | 文章生成 |
| `/api/polish` | POST | 文本润色 |
| `/api/expand` | POST | 内容扩写 |
| `/api/summarize` | POST | 摘要提取 |
| `/api/translate` | POST | 多语种翻译 |

### 用户接口

| 接口 | 方法 | 描述 |
|------|------|------|
| `/api/quota` | GET | 查询配额 |
| `/api/history` | GET | 查询历史记录 |

## 使用示例

### 注册

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456","email":"test@example.com"}'
```

### 登录

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"testuser","password":"123456"}'
```

### 生成文章

```bash
curl -X POST http://localhost:8080/api/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your-jwt-token" \
  -d '{"topic":"人工智能","style":"正式","wordCount":500}'
```

## 配置说明

### AI 提供商配置

```yaml
ai:
  type: deepseek  # deepseek 或 openai
  deepseek:
    api-key: ${DEEPSEEK_API_KEY:}
    base-url: ${DEEPSEEK_BASE_URL:https://api.deepseek.com/v1}
    model: ${DEEPSEEK_MODEL:deepseek-chat}
    timeout: ${DEEPSEEK_TIMEOUT:60000}
  openai:
    api-key: ${OPENAI_API_KEY:}
    base-url: ${OPENAI_BASE_URL:https://api.openai.com/v1}
    model: ${OPENAI_MODEL:gpt-3.5-turbo}
    timeout: ${OPENAI_TIMEOUT:60000}
```

### JWT 配置

```yaml
jwt:
  secret: ${JWT_SECRET:ai-writing-assistant-secret-key-2024}
  expiration: ${JWT_EXPIRATION:86400000}  # 24小时
```

### 默认用户配置

```yaml
app:
  default-user:
    enabled: ${APP_DEFAULT_USER_ENABLED:true}
    username: ${APP_DEFAULT_USERNAME:admin}
    password: ${APP_DEFAULT_PASSWORD:admin123}
    email: ${APP_DEFAULT_EMAIL:admin@example.com}
    daily-quota: ${APP_DEFAULT_QUOTA:100}
```

## 项目结构

```
AI-writing-assistant/
├── src/main/java/com/example/aiwriter/
│   ├── controller/          # REST API 控制器
│   ├── service/             # 业务逻辑层
│   ├── client/              # AI API 客户端
│   ├── repository/          # 数据访问层
│   ├── entity/              # JPA 实体
│   ├── dto/                 # 数据传输对象
│   ├── config/              # 配置类
│   ├── interceptor/         # 拦截器
│   ├── exception/           # 异常处理
│   ├── util/                # 工具类
│   └── AiWriterApplication.java
├── src/main/resources/
│   ├── static/              # 静态资源（前端页面）
│   └── application.yml      # 应用配置
└── pom.xml
```

## 安全说明

1. **API Key 保护**：不要将 API Key 提交到版本控制系统
2. **生产环境**：
   - 使用 MySQL 替代 H2 数据库
   - 禁用默认用户或修改默认密码
   - 设置强 JWT Secret
3. **HTTPS**：生产环境请启用 HTTPS

## 许可证

MIT License
