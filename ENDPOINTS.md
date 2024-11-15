# ENDPOINTS DOCUMENT

##### Basic URL: https://acgforum.com

## Gets:

###### Get the latest 50 posts.

#### URL: /latest-posts

#### Query parames

```
num:30	//the number of posts you want to query.
```

#### Response body

```json
{
    "posts":[
    	{
            "id": 1,
            "title": null/"Trump just announced an ...",
            "like":234,
            "commentsNum":54
        },
		{
            ...
        }
    ]
}
```

###### Get the most popular 50 posts.

#### URL: /mostLikedPosts

#### Query parames

```
num:30	//the number of posts you want to query.
```

#### Response body

```json
{
    "posts":[
    	{
            "id": 1,
            "title": null/"Trump just announced an ...",
            "like":234,
            "commentsNum":54
        },
		{
            ...
        }
    ]
}
```

###### Get the detailed Post.

#### URL: /post

#### Query params

```json
{
    "id": 16
}
```

#### Response body

```json
{
    "post":{
		"id": 1,
        "title": null/"Trump just announced an ...",
        "content": "September 1st, Trump ...",
        "like":234,
        "commentsNum":54,	
        "comentsId":[2,42,12]
    }
}
```

###### Get user‘s info

#### URL: /user-info

#### Query params

```
token: true	//means user has loged.
userId: 2	//the id of user
```

#### Response body

```json
{
	"id": 2,
	"Name": "alice",
	"avatarUrl": "/2dt3ksdf.jpn",
	"selfIntro": "I am an bishoujo...",
	"fansNum": 241,
	"subscribeNum": 23
}
```

###### Log in

#### URL: /login

Query params

```json
{
	"userName":"mike",
    "email":"2352@qq.com",
    "password":"line of password which is not being encrypted."
}
```

#### Responds body

```json
{
    "token": true/false
}
```



## Posts:

###### Put up a post.

#### URL: /put-post

#### Query body

```json
{
    "title": "Headline",
    "content": "content of this post."
}
```

###### Sign up an account.

#### URL: /sign-up

```json
{
    "userName": "lily",
    "email": "lily@qq.com",
    "password": "my password"
}
```

