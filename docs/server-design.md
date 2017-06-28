# <center>服务端设计文档</center>

## 1. 目录结构
```
yizhu
	- src
		- main
			- java
				- com.sysu.yizhu
					- business               # 软件业务包
						- entities           # 实体
							- repositories   # 实体持久层仓库
						- services           # 服务层
					- util                   # 工具
					- web                    # web层
						- controller         # 接口控制器路由
						- interceptor        # 拦截器
			- resources                      # 配置资源文件
			- webapp
				- WEB-INF                    # 配置文件
		- test
			- java
				- test
	- pom.xml                                # 项目管理
```

## 2. 外部系统
```
database
	mysql
		user: root
		pass: 123456
```

## 3. 控制器路由
### 3.1 实现
```
# 位于com.sysu.yizhu.web.controller包中
	- UserController.java        # 实现用户类接口
	- QuestionController.java    # 实现提问、回答类接口
	- SOSController.java         # 实现一键求救接口
	- HelpController.java        # 实现求助功能接口
```

接口详情见接口文档api.md
[https://github.com/SYSU-yizhu/yizhu/blob/master/docs/api.md](https://github.com/SYSU-yizhu/yizhu/blob/master/docs/api.md "api.md")

### 3.2 返回值
```
# 位于com.sysu.yizhu.util包中
# 继承自LinkedHashMap<String, Object>，通过Spring返回值格式化为JSON格式
	- ReturnMsg.java
```

### 3.3 拦截器
```
# 位于com.sysu.yizhu.web.interceptor包中
	- LoginInterceptor.java
```
通过拦截用户请求，区分请求方法，POST方法需要登录。
将用户记录于session中。

## 4. 业务层
### 4.1 向上接口实现
```
# 位于com.sysu.yizhu.business.services包中
	- UserService.java
	- QuestionService.java
	- SOSService.java
	- HelpService.java
```
介于数据库持久层与控制器层中间，集成数据库操作并向控制器提供业务接口。

### 4.2 实体
```
# 位于com.sysu.yizhu.business.entities包中
# 实体设计
-  User
-  Comment
-  Question
-  Answer
-  AgreeAnswer
-  SOS
-  SOSResponse
-  Help
-  HelpResponse


# 持久层设计
- repositories
```

详情见数据库设计
[https://github.com/SYSU-yizhu/yizhu/blob/master/docs/database_design.png](https://github.com/SYSU-yizhu/yizhu/blob/master/docs/database_design.png "database_design.png")

#### 框架
使用Hibernate持久层框架，将实体映射为数据库的表结构。


## 5. 工具类
```
# 位于com.sysu.yizhu.util包中
```
### 5.1 推送、短信服务
```
# 使用leancloud提供的服务
- LCConfig   # 读取leancloud配置数据，如app-key
- LCUtil     # 自定义封装leancloud服务，便于调用
```
封装了：短信验证服务；消息推送服务。

### 5.2 MD5计算
```
- MD5Parser
	- getMD5()    # 将传入字符串计算MD5值并返回
```

### 5.3 数据库MySQL自定义连接配置类
```
- MySQL5Dialect   # 配置了innoDB和建表编码utf-8
```

### 5.4 手机号码检验
```
- PhoneNumUtil
	- isPhone()   # 检验传入手机号是否为有效格式的手机号
```
检验使用了正则表达式。

### 5.5 返回消息
见控制器层叙述。


## 6. 配置资源文件
```
# 位于yizhu/src/main/resources中
```

### 6.1 LeanCloud配置文件
```
- leancloud.properties    # 需要X-LC-Id，X-LC-Key字段，由leancloud官网提供
```

### 6.2 log4j日志系统配置
```
- log4j.properties        # 配置log4j插件的具体项
```
使用AOP的编程模式。

### 6.3 各运行环境配置
```
- settings-development.properties   # 开发环境配置
- settings-jenkins.properties       # 集成测试环境配置
- settings-production.properties    # 正式环境配置

# 均有字段
db.user
db.password
db.jdbcUrl

```


## 7. 系统运行部署

### 7.1 项目github地址
```
https://github.com/SYSU-yizhu/yizhu
```

### 7.2 mysql数据库设置
```
见本文档 6.3 配置文件，配置数据库端口
```

### 7.3 运行
```
# 如使用production配置
# 进入yizhu项目文件夹，运行命令行
mvn clean package tomcat7:run -Dspring.profiles.active=production
```

### 7.4 集成环境配置，见博客
[简单CI 多docker连接](http://119.29.166.163/index.php/2017/06/28/jenkins-%E7%AE%80%E5%8D%95ci-%E5%A4%9Adocker%E8%BF%9E%E6%8E%A5/)