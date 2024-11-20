import React, { useEffect, useState } from "react";
import './PostList.css'

export default function PostList() {
    const [isPending, setIsPending] = useState(true);
    const [posts, setPosts] = useState(null);


    useEffect(() => {
        fetch('http://localhost:8080/latest-posts')
            .then(response => response.json())
            .then(data => {
                setPosts(data);
                setIsPending(false);
            })
            .catch(err => {
                console.log(err);
            });

    }, [])




    return (
        <div className='container'>
            {isPending ? (
                <p>Loading...</p>
            ) : (
                posts.map(post => (
                    <a href={post.id} key={post.id}>
                        <div className='post'>
                            <h4>{post.title}</h4>
                            <p>Likes: {post.like} {`\u00A0\u00A0\u00A0\u00A0`}Comments: {post.commentsNum}</p>
                        </div>
                    </a>
                ))
            )}
        </div>
    );


}