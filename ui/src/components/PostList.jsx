import React, { useEffect, useState } from "react";
import './PostList.css'
import { redirect } from "react-router-dom";

export default function PostList() {
    const [isPending, setIsPending] = useState(true);
    const [posts, setPosts] = useState([]);
    const [page, setPage] = useState(1);

    useEffect(() => {
        var url = 'http://121.40.20.237:4433/latest_post/' + page;

        var requestOptions = {
            method: 'GET',
            redirect: 'follow'
        };

        fetch(url, requestOptions)
            .then(response => response.json())
            .then(data => {
                setPosts(data);
                setIsPending(false);
            })
            .catch(error => {
                console.error('error', error);
                setIsPending(false); // Set loading to false even if there's an error
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
