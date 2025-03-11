# ACG Forum

Base URLs: http://www.yourdomainname.com

# Authentication

# Default

## GET /post/{id}

GET /post/{id}

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|path|integer| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET /user/{id}

GET /user/{id}

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|path|integer| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET /latest_posts

GET /latest_post/{page}

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|page|path|string| 是 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST /most_liked_posts

POST /most_liked_posts

> Body 请求参数

```json
{
  "page": 1,
  "interval": "week"
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST /put_post

POST /put_post

> Body 请求参数

```json
{
  "title": "Headline",
  "content": "content of this post.",
  "userId": 2,
  "isRoot": true,
  "root": 1,
  "forumId": 1
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST /signup

POST /signup

> Body 请求参数

```json
{
  "userName": "mike",
  "email": "2352@qq.com",
  "password": "line of password which is not being encrypted."
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST /login

POST /login

> Body 请求参数

```json
{
  "email": "2352@qq.com",
  "password": "line of password which is not being encrypted."
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT /like/{id}

PUT /like_post/

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|userId|query|string| 否 |none|
|postId|query|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE /delete_post

DELETE /delete_post/{id}

> Body 请求参数

```json
""
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|path|string| 是 |none|
|body|body|object| 否 |none|

> 返回示例

```json
{
  "isOk": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE /delete_user

DELETE /delete_user/{id}

> Body 请求参数

```json
""
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|path|string| 是 |none|
|body|body|object| 否 |none|

> 返回示例

```json
{
  "isOk": "ok"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT /collect_post

PUT /collect_post

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|userId|query|string| 否 |none|
|postId|query|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT /subscribe_user

PUT /subscribe_user

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|myId|query|string| 否 |none|
|userId|query|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET /collected_post

GET /collected_posts

> Body 请求参数

```json
""
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|userId|query|string| 否 |ID 编号|
|page|query|string| 否 |第{page}页|
|body|body|object| 否 |none|

> 返回示例

```json
[
  {
    "id": 1,
    "title": "exampleTile",
    "like": 32,
    "commentsNum": 3
  },
  {
    "id": 2,
    "title": "newTitle!",
    "like": 12,
    "commentsNum": 32
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» id|integer|true|none||none|
|» title|string|true|none||none|
|» like|integer|true|none||none|
|» commentsNum|integer|true|none||none|

## GET /fans

GET /fans/{myId}

> Body 请求参数

```json
""
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|myId|path|string| 是 |none|
|body|body|object| 否 |none|

> 返回示例

```json
[
  {
    "id": 2,
    "userName": "jack",
    "email": "jack@qq.com",
    "selfIntro": "sdlf...",
    "avatarUrl": null,
    "phoneNum": 1234124,
    "hc_password": 3412351,
    "gender": true,
    "age": 32,
    "fanNum": 1,
    "subscribeNum": 1
  },
  {
    "id": 3,
    "userName": "alice",
    "email": "alice@qq.com",
    "selfIntro": "sdlf...",
    "avatarUrl": null,
    "phoneNum": 1234124,
    "hc_password": 3412351,
    "gender": false,
    "age": 32,
    "fanNum": 0,
    "subscribeNum": 2
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

状态码 **200**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» id|integer|true|none||none|
|» userName|string|true|none||none|
|» email|string|true|none||none|
|» selfIntro|string|true|none||none|
|» avatarUrl|null|true|none||none|
|» phoneNum|integer|true|none||none|
|» hc_password|integer|true|none||none|
|» gender|boolean|true|none||none|
|» age|integer|true|none||none|
|» fanNum|integer|true|none||none|
|» subscribeNum|integer|true|none||none|

## GET /subposts/{id}

GET /subposts/

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|query|string| 否 |ID 编号|
|page|query|string| 否 |第{page}页|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET /subscribed_users

GET /subscribed_users

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|id|query|string| 否 |ID 编号|
|page|query|string| 否 |第{page}页|

> 返回示例

```json
[
  {
    "id": 1,
    "userName": "mike",
    "email": "mike@qq.com",
    "selfIntro": null,
    "avatarUrl": "defaultAvatar.jpg",
    "phoneNum": null,
    "hc_password": -138263229,
    "gender": null,
    "age": null,
    "fanNum": 1,
    "subscribeNum": 0
  },
  {
    "id": 2,
    "userName": "alice",
    "email": "alice@qq.com",
    "selfIntro": null,
    "avatarUrl": "defaultAvatar.jpg",
    "phoneNum": null,
    "hc_password": 279467670,
    "gender": null,
    "age": null,
    "fanNum": 1,
    "subscribeNum": 0
  }
]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET /posts/{authorId}

GET /posts_by_author

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|authorId|query|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET /search

GET /search_post

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|keyword|query|string| 否 |none|

> 返回示例

> 404 Response

```json
{
  "apifoxError": {
    "code": 0,
    "message": "string"
  }
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|none|Inline|

### 返回数据结构

状态码 **404**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» apifoxError|object|true|none||none|
|»» code|integer|true|none||none|
|»» message|string|true|none||none|

## GET search_user

GET /search_user

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|keyword|query|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE unsubscribe_user

DELETE /unsubscribe_user

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|myId|query|string| 否 |none|
|userId|query|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## DELETE uncollect_post

DELETE /uncollect_post

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|userId|query|string| 否 |ID 编号|
|postId|query|string| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET /posts_by_forum

GET /posts_by_forum

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|forumId|query|string| 否 |ID 编号|
|page|query|string| 否 |第{page}页|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## GET /forums/{page}

GET /forums/{page}

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|page|path|string| 是 |none|

> 返回示例

```json
[
  {
    "forumId": 1,
    "forumName": "game",
    "introduction": "this is a forum ...",
    "userNum": 4122,
    "icon": "defaultIcon.jpg"
  },
  {
    "forumId": 2,
    "forumName": "anime",
    "introduction": "this is a forum ...",
    "userNum": 23452,
    "icon": "defaultIcon.jpg"
  }
]
```

```json
[]
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST create_forum

POST /create_forum

> Body 请求参数

```json
{
  "forumName": "game",
  "owner": "3",
  "introduction": null
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|

> 返回示例

```json
{
  "isOk": false,
  "forumId": null,
  "msg": "this forum was already been used."
}
```

```json
{
  "isOk": true,
  "forumId": 32,
  "msg": "create forum success."
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|404|[Not Found](https://tools.ietf.org/html/rfc7231#section-6.5.4)|none|Inline|

### 返回数据结构

状态码 **404**

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|» apifoxError|object|true|none||none|
|»» code|integer|true|none||none|
|»» message|string|true|none||none|

## POST /change_avatar/{userId}

POST /change_avatar/{userId}

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|userId|path|string| 是 |none|

> 返回示例

```json
{
  "isOk": true
}
```

```json
{
  "timestamp": "2023-02-20T14:30:00.000+08:00",
  "status": 413,
  "error": "Payload Too Large",
  "message": "Maximum upload size exceeded; nested exception is java.lang.IllegalStateException: org.springframework.web.multipart.MultipartException: Could not parse multipart servlet request; nested exception is java.io.IOException: org.springframework.web.multipart.MultipartException: Could not parse multipart servlet request; nested exception is java.io.IOException: Maximum upload size exceeded",
  "path": "/change_avatar/123"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## POST /change_icon/{forumId}

POST /change_icon/{forumId}

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|forumId|path|string| 是 |none|

> 返回示例

```json
{
  "isOk": true
}
```

```json
{
  "timestamp": "2023-02-20T14:30:00.000+08:00",
  "status": 413,
  "error": "Payload Too Large",
  "message": "Maximum upload size exceeded; nested exception is java.lang.IllegalStateException: org.springframework.web.multipart.MultipartException: Could not parse multipart servlet request; nested exception is java.io.IOException: org.springframework.web.multipart.MultipartException: Could not parse multipart servlet request; nested exception is java.io.IOException: Maximum upload size exceeded",
  "path": "/change_avatar/123"
}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

## PUT /alter_user_Intro

PUT /alter_user_intro

> Body 请求参数

```json
{
  "userId": 3,
  "newIntro": "i am a handsome guy comes from ..."
}
```

### 请求参数

|名称|位置|类型|必选|说明|
|---|---|---|---|---|
|body|body|object| 否 |none|

> 返回示例

> 200 Response

```json
{}
```

### 返回结果

|状态码|状态码含义|说明|数据模型|
|---|---|---|---|
|200|[OK](https://tools.ietf.org/html/rfc7231#section-6.3.1)|none|Inline|

### 返回数据结构

# 数据模型

<h2 id="tocS_Pet">Pet</h2>

<a id="schemapet"></a>
<a id="schema_Pet"></a>
<a id="tocSpet"></a>
<a id="tocspet"></a>

```json
{
  "id": 1,
  "category": {
    "id": 1,
    "name": "string"
  },
  "name": "doggie",
  "photoUrls": [
    "string"
  ],
  "tags": [
    {
      "id": 1,
      "name": "string"
    }
  ],
  "status": "available"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer(int64)|true|none||宠物ID编号|
|category|[Category](#schemacategory)|true|none||分组|
|name|string|true|none||名称|
|photoUrls|[string]|true|none||照片URL|
|tags|[[Tag](#schematag)]|true|none||标签|
|status|string|true|none||宠物销售状态|

#### 枚举值

|属性|值|
|---|---|
|status|available|
|status|pending|
|status|sold|

<h2 id="tocS_Category">Category</h2>

<a id="schemacategory"></a>
<a id="schema_Category"></a>
<a id="tocScategory"></a>
<a id="tocscategory"></a>

```json
{
  "id": 1,
  "name": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer(int64)|false|none||分组ID编号|
|name|string|false|none||分组名称|

<h2 id="tocS_Tag">Tag</h2>

<a id="schematag"></a>
<a id="schema_Tag"></a>
<a id="tocStag"></a>
<a id="tocstag"></a>

```json
{
  "id": 1,
  "name": "string"
}

```

### 属性

|名称|类型|必选|约束|中文名|说明|
|---|---|---|---|---|---|
|id|integer(int64)|false|none||标签ID编号|
|name|string|false|none||标签名称|

