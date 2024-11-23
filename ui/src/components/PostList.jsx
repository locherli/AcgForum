import React, { useEffect, useState } from "react";
import './PostList.css'

export default function PostList() {
    const [isPending, setIsPending] = useState(true);
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        var myHeaders = new Headers();
        myHeaders.append("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Accept", "*/*");
        myHeaders.append("Host", "apifoxmock.com");
        myHeaders.append("Connection", "keep-alive");
        
        var raw = JSON.stringify({});
        
        var requestOptions = {
           method: 'POST',
           headers: myHeaders,
           body: raw,
           redirect: 'follow'
        };
        
        fetch("https://apifoxmock.com/m1/5470794-5146307-default/latest_posts", requestOptions)
           .then(response => {
               if (!response.ok) {
                   throw new Error('Network response was not ok');
               }
               return response.json();
           })
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
