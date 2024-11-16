import React from "react";
import './PostList.css'

export default function PostList() {

    const responseBody = {
        "posts": [
            {
                "id": 1,
                "title": "Trump just announced an ...",
                "like": 234,
                "commentsNum": 54
            },
            {
                "id": 1,
                "title": null,
                "like": 234,
                "commentsNum": 54
            }
        ]
    }

    return <>
        <div className='container'>
            {responseBody.posts.map(post => (
                <a href="#">
                    <div key={post.id} className='post'>
                        <h4>{post.title}</h4>
                        <p>Likes: {post.like}</p>
                        <p>Comments: {post.commentsNum}</p>
                    </div>
                </a>

            ))}
        </div>
    </>
}