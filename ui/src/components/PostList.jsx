import React, { useEffect, useState } from "react";
import './PostList.css'

export default function PostList() {
        //test example
        const testExample={
            "posts": [
                {
                    "id": 1,
                    "title": "Trump just announced an ...",
                    "like": 234,
                    "commentsNum": 54
                },
                {
                    "id": 2,
                    "title": "Example 2",
                    "like": 234,
                    "commentsNum": 54
                }
            ]
        }

    const [posts, setPosts] = useState(testExample.posts);

    useEffect(() => {
        // fetch('http://localhost:8080/blogs')
        //     .then(response => response.json())
        //     .then(response => setPosts(response.posts))
        //     .catch(err => {
        //         console.log(err);
        //     });
    
    }, []);

    console.log(posts);
    


    return <>
        <div className='container'>
            {posts.map(post => (
                <a href={post.id}>
                    <div key={post.id} className='post'>
                        <h4>{post.title}</h4>
                        <p>Likes: {post.like} {`\u00A0\u00A0\u00A0\u00A0`}Comments: {post.commentsNum}</p>

                    </div>
                </a>

            ))}
        </div>
    </>
}