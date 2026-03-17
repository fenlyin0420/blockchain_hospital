# 区块链医院管理系统 (Blockchain Hospital Management System)

基于区块链技术的医院信息管理系统，支持病历加密、环签名隐私保护、跨院转诊等功能。

## 项目概述

本项目是一个完整的医院管理系统，采用前后端分离架构，集成了区块链相关技术以保障医疗数据的安全性和隐私性。系统支持管理员、医生、护士、患者四种角色，提供全面的医院业务管理功能。

## 技术栈

### 后端
- **Java 17**
- **Spring Boot 3.0.3**
- **MyBatis** - 数据持久化
- **MySQL 8.0** - 数据库
- **JWT** - 身份认证
- **BouncyCastle** - 国密加密算法
- **Hutool** - Java工具库
- **ZXing** - 二维码生成
- **Apache POI** - Excel处理

### 前端
- **Vue.js 2.6**
- **Vue Router** - 路由管理
- **Vuex** - 状态管理
- **Element UI** - UI组件库
- **Axios** - HTTP请求
- **ECharts** - 数据可视化
- **Markdown-it** - Markdown渲染

## 主要功能

### 用户管理
- 管理员管理
- 医生信息管理
- 护士信息管理
- 患者信息管理
- 个人中心

### 医疗业务
- 科室管理
- 医生排班
- 预约挂号
- 就诊记录
- 住院登记
- 病房管理
- 床位分配

### 病历管理
- 病历列表
- 病历详情
- 病历历史
- 病历加密（国密算法）
- 病历签名（环签名技术）
- 档案检索
- 权限管理

### 护理管理
- 护士排班
- 日常护理记录
- 健康检测
- 护理计划

### 转诊系统
- 转诊申请
- 转出审批
- 转入审核
- 病历传输
- 转诊记录

### 其他功能
- 药品管理
- 公告通知
- 系统日志监控
- 密钥管理

## 项目结构

```
blockchain_hospital/
├── springboot/                 # 后端项目
│   ├── src/main/java/com/example/
│   │   ├── common/            # 公共模块
│   │   │   ├── config/        # 配置类
│   │   │   └── enums/         # 枚举类
│   │   ├── controller/        # 控制器
│   │   ├── service/           # 服务层
│   │   ├── mapper/            # MyBatis Mapper
│   │   ├── entity/            # 实体类
│   │   ├── dto/               # 数据传输对象
│   │   ├── exception/         # 异常处理
│   │   └── utils/             # 工具类
│   └── src/main/resources/
│       ├── mapper/            # MyBatis XML
│       └── application.yaml   # 配置文件
├── vue/                       # 前端项目
│   ├── src/
│   │   ├── views/             # 页面组件
│   │   │   ├── manager/       # 管理后台页面
│   │   │   ├── front/         # 前台页面
│   │   │   ├── case/          # 病历相关
│   │   │   ├── records/       # 档案相关
│   │   │   ├── referral/      # 转诊相关
│   │   │   └── component/     # 公共组件
│   │   ├── router/            # 路由配置
│   │   └── App.vue            # 根组件
│   └── package.json
└── files/                     # 上传文件存储
```

## 快速开始

### 环境要求
- JDK 17+
- Node.js 16+
- MySQL 8.0+
- Maven 3.6+

### 后端启动

1. 创建数据库并导入SQL脚本
```bash
mysql -u root -p < hospital.sql
```

2. 修改数据库配置
编辑 `springboot/src/main/resources/application.yaml`，配置数据库连接信息。

3. 启动后端服务
```bash
cd springboot
mvn spring-boot:run
```

后端服务将在 `http://localhost:9090` 启动。

### 前端启动

1. 安装依赖
```bash
cd vue
npm install
```

2. 启动开发服务器
```bash
npm run serve
```

前端服务将在 `http://localhost:8080` 启动。

## 用户角色

| 角色 | 权限说明 |
|------|----------|
| Admin | 系统管理员，拥有所有权限 |
| Doctor | 医生，管理患者、病历、排班等 |
| Nurse | 护士，管理护理计划、床位分配等 |
| User | 患者，查看个人信息、病历、预约挂号等 |

## 安全特性

- **JWT认证**: 基于Token的身份验证
- **国密加密**: 采用SM2/SM3/SM4国密算法保护敏感数据
- **环签名**: 支持环签名技术保护病历隐私
- **数据加密**: 病历敏感字段自动加密存储

## API文档

主要API端点：

- `/login` - 用户登录
- `/register` - 用户注册
- `/admin/*` - 管理员接口
- `/doctor/*` - 医生接口
- `/nurse/*` - 护士接口
- `/user/*` - 患者接口
- `/record/*` - 病历接口
- `/referral/*` - 转诊接口

## 开发说明

- 后端采用RESTful API设计
- 前端采用组件化开发
- 支持热重载开发模式
- 日志记录用户操作行为

## License

MIT License