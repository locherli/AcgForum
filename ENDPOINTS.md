# ENDPOINTS DOCUMENT

##### Basic URL: https://acgforum.com

# Gets:

###### Get the latest posts.

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

###### Get the most popular posts.

#### URL: /most-liked-posts

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
        "commentsId":[2,42,12]
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
  "Name": "Alice",
  "avatarUrl": "https://example.com/2dt3ksdf.jpg",
  "selfIntro": "I am a bishoujo...",
  "fansNum": 241,
  "subscribeNum": 23
}

```





------

------

------



# Posts:

###### Put up a post.

#### URL: /put-post

#### Query body

```json
{
    "title": "Headline",
    "content": "content of this post.",
    "userId": 2
}
```

###### Like 

###### Sign up an account.

#### URL: /sign-up

```json
{
    "userName": "lily",
    "email": "lily@qq.com",
    "password": "my password"
}
```

###### Log in

#### URL: /login

#### Query body

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

