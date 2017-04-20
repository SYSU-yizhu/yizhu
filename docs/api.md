### 注册
| Code | Content | Description |
|------|---------|-------------|
|200|OK|请求成功|
|400|FAILED|用户名已存在|
|403|FORBIDDEN|用户名不是手机号|

URI:

```
POST /user/register
```

POST参数

| 字段 | 描述 | 类型 |
|----------|-------------|------|
|userId|用户名（手机号）|string|
|password|密码（未处理）|string|


成功例子：

```json
{
	"userId":"11111111111"
}
```


### 登录
| Code | Content | Description |
|------|---------|-------------|
|200|OK|请求成功|
|404|NOT FOUND|用户名或密码错误|

URI:

```
POST /user/login
```

POST参数

| 字段 | 描述 | 类型 |
|----------|-------------|------|
|userId|用户名（手机号）|string|
|password|密码（未MD5处理）|string|


成功例子：
```json
{
	"userId":"11111111111",
	"password":"tZxnvxlqR1gZHkL3ZnDOug=="
}
```

### 一键求救（推送、导航、评价）


### 求助 （图片、音频、文字、视频、评价）


### 提问



### 个人基本信息