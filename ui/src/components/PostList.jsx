import React, { useEffect, useState } from "react";
import './PostList.css'
import { Link, redirect, useLocation } from "react-router-dom";
import { Cookies } from "react-cookie";

export default function LatestPostList() {
    const [isPending, setIsPending] = useState(true);
    const [posts, setPosts] = useState([]);
    const [page, setPage] = useState(1);

    useEffect(() => {
        setIsPending(true); // 开始加载数据
        const url = `http://${window.basicUrl}/latest_post/${page}`;

        fetch(url, { method: 'GET', redirect: 'follow' })
            .then(response => response.json())
            .then(data => {
                setPosts(data);
                setIsPending(false);
            })
            .catch(error => {
                console.error('error', error);
                setIsPending(false);
            });
    }, [page]) // 添加 page 作为依赖项

    function prePage() {
        setPage(prev => Math.max(prev - 1, 1)); // 确保页码不小于1
    }

    function nextPage() {
        setPage(prev => prev + 1);
    }

    return (
        <div className='container' style={{ margin: '20px' }}>
            {isPending ? (
                <p>Loading...</p>
            ) : (posts.length === 0 ?
                <p style={{ color: 'white', display: 'flex', justifyContent: 'space-evenly' }}>无</p> :
                posts.map(post => (
                    <Link to={`/detailedpost/?id=${post.id}`} key={post.id} style={{ marginTop: '10px' }}>
                        <div className='post'>
                            <h4>{post.title}</h4>
                            <p>Likes: {post.like} {`\u00A0\u00A0\u00A0\u00A0`}Comments: {post.commentsNum}</p>
                        </div>
                    </Link>
                ))
            )}

            {/* 翻页导航 */}
            <ul style={{ display: 'flex', listStyle: 'none', justifyContent: 'space-evenly' }} id='nav'>
                <li>
                    <button
                        onClick={prePage}
                        disabled={page === 1} // 第一页时禁用上一页按钮
                    >
                        上一页
                    </button>
                </li>

                <li>
                    <button onClick={nextPage}>
                        下一页
                    </button>
                </li>
            </ul>
            <span style={{ color: 'white', display: 'flex', justifyContent: 'space-evenly' }}>当前页：{page}</span>
        </div>
    );
}

export function CollectedPostList() {
    const [isPending, setIsPending] = useState(true);
    const [posts, setPosts] = useState([]);
    const [page, setPage] = useState(1);

    useEffect(() => {
        const cookie = new Cookies();
        var url = 'http://' + window.basicUrl + '/collected_posts?userId=' + cookie.get('userId') + '&page=' + page;

        var requestOptions = {
            method: 'GET',
            redirect: 'follow'
        };

        fetch(url, requestOptions)
            .then(response => response.json())
            .then(data => {
                setPosts(data);
                setIsPending(false);
                setPage(page + 1);
            })
            .catch(error => {
                console.error('error', error);
                setIsPending(false); // Set loading to false even if there's an error
            });
    }, [])

    function handleUncollect(id) {
        const cookie = new Cookies();
        fetch(`http://${window.basicUrl}/uncollect_post?userId=${cookie.get('userId')}&postId=${id}`)
            .catch(err => {
                console.log(err); alert('网络异常！'); return;
            });
        alert('成功取消收藏');
        document.location.reload();
    }

    return (
        <div className='container' style={{ width: '40vw' }}>
            {isPending ? (
                <p>Loading...</p>
            ) : (
                posts.length === 0 ? <p>尚未收藏帖子</p> : posts.map(post => (
                    <div className='post' key={post.id}>
                        <Link to={`/detailedpost?id=${post.id}`}>
                            <h4>{post.title}</h4>
                            <p>Likes: {post.like} {`\u00A0\u00A0\u00A0\u00A0`}Comments: {post.commentsNum}</p>
                        </Link>
                        <button onClick={() => { handleUncollect(post.id) }}
                            style={{
                                width: '80px',
                                height: '30px',
                                position: 'relative',
                                left: '30vw',
                                bottom: '70px',
                                backgroundColor: 'rgba(255,255,255,0.5)',
                                border: 'none',
                                borderRadius: '10px',
                                cursor: 'pointer'
                            }} >取消收藏</button>
                    </div>
                ))
            )}
        </div>
    );
}


export function MyPostList() {
    const [isPending, setIsPending] = useState(true);
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        const cookie = new Cookies();
        var url = 'http://' + window.basicUrl + '/posts_by_author?authorId=' + cookie.get('userId');

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

    function handleDeletePost(id) {
        fetch(`http://${window.basicUrl}/delete_post/${id}`)
            .catch(err => {
                console.log(err); alert('网络异常！'); return;
            });
        alert('成功删除');
        window.location.reload();
    }

    return (
        <div className='container' style={{ width: '40vw' }}>
            {isPending ? (
                <p>Loading...</p>
            ) : (
                posts.length === 0 ? <p>尚未发布帖子</p> : posts.map(post => (
                    <div className='post' key={post.id}>
                        <Link to={`/detailedpost?id=${post.id}`}>
                            <h4>{post.title}</h4>
                            <p>Likes: {post.like} {`\u00A0\u00A0\u00A0\u00A0`}Comments: {post.commentsNum}</p>
                        </Link>
                        <button onClick={() => { handleDeletePost(post.id) }}
                            style={{
                                width: '50px',
                                height: '30px',
                                position: 'relative',
                                left: '30vw',
                                bottom: '70px',
                                backgroundColor: 'rgba(255,255,255,0.5)',
                                border: 'none',
                                borderRadius: '10px',
                                cursor: 'pointer'
                            }} >删除</button>
                    </div>
                ))
            )}
        </div>
    );
}

// export function ForumPosts(){
//         const location = useLocation();
//         const searchParams = new URLSearchParams(location.search);
//         const forumId = searchParams.get('id');
//         const [isPending, setIsPending] = useState(true);
//         const [forum, setForum] = useState([]);


//         useEffect(()=>{

// var requestOptions = {
//    method: 'GET',
//    redirect: 'follow'
// };

// fetch(`http://${window.basicUrl}/forum/`, requestOptions)
//    .then(response => response.text())
//    .then(result => console.log(result))
//    .catch(error => console.log('error', error));
//         },[])
// }