

# 一、用户类

## 注册前发送短信验证码
| Code | Content | Description |
|------|---------|-------------|
|200|OK|请求成功|
|400|FAILED|手机号已存在|
|403|FORBIDDEN|手机号（用户名）无效|

URI:

```
GET /user/sendSms/:userId
```

GET参数

| 字段 | 描述 | 类型 |
|----------|-------------|------|
|userId|用户名（手机号）|string|


成功例子：

```json
{
	"userId":"11111111111"
}
```

## 注册
| Code | Content | Description |
|------|---------|-------------|
|200|OK|请求成功|
|400|FAILED|用户名已存在|
|403|FORBIDDEN|用户名不是手机号或参数格式错误|
|450|MISS|验证码错误|

URI:

```
POST /user/register
```

POST参数

| 字段 | 描述 | 类型 |
|----------|-------------|------|
|userId|用户名（手机号）|string|
|password|密码（未处理）|string|
|code|短信验证码|string|
|name|姓名|string|
|gender|性别|string - "male"/"female"|
|birthDate|出生日期|string - YYYY-MM-DD|
|location|常住地|string|


成功例子：

```json
{
	"userId":"11111111111"
}
```

## 登录
| Code | Content | Description |
|------|---------|-------------|
|200|OK|请求成功|
|403|FORBIDDEN|用户名不是手机号或参数格式错误|
|404|NOT FOUND|用户名或密码错误|

URI:

```
POST /user/login
```

POST参数

| 字段 | 描述 | 类型 |
|----------|-------------|------|
|userId|用户名（手机号）|string|
|password|密码（未处理）|String|


成功例子：
```json
{
	"userId":"11111111111"
}
```

## 修改个人信息
| Code | Content | Description |
|------|---------|-------------|
|200|OK|请求成功|
|404|FAILED|用户名或密码错误|
|403|FORBIDDEN|用户名不是手机号或参数格式错误|

URI:

```
POST /user/modifyInfo
```

POST参数

| 字段 | 描述 | 类型 |
|----------|-------------|------|
|userId|用户名（手机号）|string|
|password|密码（未处理）|string|
|name|姓名|string|
|gender|性别|string - "male"/"female"|
|birthDate|出生日期|string - YYYY-MM-DD|
|location|常住地|string|


成功例子：

```json
{
	"userId":"11111111111"
}
```

## 获取个人信息
| Code | Content | Description |
|------|---------|-------------|
|200|OK|请求成功|
|404|FAILED|用户名或密码错误|
|403|FORBIDDEN|用户名不是手机号或参数格式错误|

URI:

```
POST /user/info
```

POST参数

| 字段 | 描述 | 类型 |
|----------|-------------|------|
|userId|用户名（手机号）|string|
|password|密码（未处理）|string|


成功例子：

```json
{
	"userId":"11111111111",
	"name":"张三",
	"gender":"male",
	"birthDate":"1995-12-02",
	"location":"中山大学"	
}
```


# 二、提问类

## 提问
| Code | Content | Description |
|------|---------|-------------|
|200|OK|请求成功|
|403|FORBIDDEN|用户名不是手机号或参数格式错误|
|404|NOT FOUND|用户名或密码错误|

URI:

```
POST /question/ask
```

POST参数

| 字段 | 描述 | 类型 |
|----------|-------------|------|
|userId|用户名（手机号）|string|
|password|密码（未处理）|String|
|title|标题|string|
|content|提问内容|string|

成功例子：
```json
{
	"questionId":1
}
```

## 回答
| Code | Content | Description |
|------|---------|-------------|
|200|OK|请求成功|
|403|FORBIDDEN|用户名不是手机号或参数格式错误|
|404|NOT FOUND|用户名或密码错误|
|450|MISS|该提问id不存在|

URI:

```
POST /question/answer
```

POST参数

| 字段 | 描述 | 类型 |
|----------|-------------|------|
|userId|用户名（手机号）|string|
|password|密码（未处理）|String|
|questionId|问题id|int|
|content|回答内容|string|


成功例子：
```json
{
	"answerId":1
}
```

## 赞同
| Code | Content | Description |
|------|---------|-------------|
|200|OK|请求成功|
|403|FORBIDDEN|用户名不是手机号或参数格式错误|
|404|NOT FOUND|用户名或密码错误|
|450|MISS|该回答id不存在|

URI:

```
POST /question/agreeAnswer
```

POST参数

| 字段 | 描述 | 类型 |
|----------|-------------|------|
|userId|用户名（手机号）|string|
|password|密码（未处理）|string|
|answerId|回答id|int|
|agreeOrNot|是否赞同|bool - true赞同/false不赞同|

成功例子：
```json
{
	"answerAgreeId":1
}
```

## 获取所有问题Id
| Code | Content | Description |
|------|---------|-------------|
|200|OK|请求成功

URI:

```
GET /question/getAllId
```


成功例子：
```json
{
	"count":2,
	"data":[1,2]
}
```

## 根据问题Id获取问题摘要
| Code | Content | Description |
|------|---------|-------------|
|200|OK|请求成功|
|404|NOT FOUND| questionId不存在|

URI:

```
GET /question/digest/{questionId}
```


成功例子：
```json
{
	"questionId":1,
	"userId":"133133123456",
	"userName":"张三",
	"title":"扶老奶奶过马路是一种怎样的体验？",
	"createDate":"2017-05-17"
}
```

## 根据问题Id获取回答所有Id
| Code | Content | Description |
|------|---------|-------------|
|200|OK|请求成功|
|404|NOT FOUND| questionId不存在|

URI:

```
GET /question/getAnswerIds/{questionId}
```


成功例子：
```json
{
	"count":2,
	"data":[1,2]
}
```

## 根据回答Id获取回答内容
| Code | Content | Description |
|------|---------|-------------|
|200|OK|请求成功|
|404|NOT FOUND|answerId不存在|

URI:

```
GET /question/getAnswer/{answerId}
```


成功例子：
```json
{
	"questionId":1,
	"userId":"133133123456",
	"userName":"张三",
	"content":"无可奉告",
	"createDate":"2017-05-17"
	"good":5,
	"bad":30
}
```


# 一键求救（推送、导航、评价）


# 求助 （图片、音频、文字、视频、评价）




# 个人基本信息