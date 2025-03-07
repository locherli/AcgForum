import { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { Link } from "react-router-dom";

export default function ForumPage() {
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const forumId = searchParams.get('id');
    const [isPending, setIsPending] = useState(true);
    const [forum, setForum] = useState([]);
    const [posts, setPosts] = useState([]);
    const [isPendingPosts, setIsPendingPosts] = useState(true);
    const [page, setPage] = useState(1);

    useEffect(() => {
        var requestOptions = {
            method: 'GET',
            redirect: 'follow'
        };

        fetch(`http://${window.basicUrl}/forum/${forumId}`, requestOptions)
            .then(response => response.json())
            .then(result => {
                setForum(result)
                setIsPending(false)
            })
            .catch(error => console.log('error', error));
    }, []);

    useEffect(() => {
        setIsPendingPosts(true); // 开始加载数据
        const url = `http://${window.basicUrl}/posts_by_forum?forumId=${forumId}&page=${page}`;

        fetch(url, { method: 'GET', redirect: 'follow' })
            .then(response => response.json())
            .then(data => {
                setPosts(data);
                setIsPendingPosts(false);
            })
            .catch(error => {
                console.error('error', error);
                setIsPendingPosts(false);
            });
    }, [page]) // 添加 page 作为依赖项

    function prePage() {
        setPage(prev => Math.max(prev - 1, 1)); // 确保页码不小于1
    }

    function nextPage() {
        setPage(prev => prev + 1);
    }

    return <div style={{
        width: '100%',
        height: 'auto',
        display: 'flex',
        justifyContent: 'center',
        color: 'white'
    }}>
        <div style={{
            backgroundColor: 'rgba(0,0,0,0.3)',
            width: '80vw'
        }}>
            {isPending ? (<p>Loading...</p>) :
                <>
                    <img src={`http://${window.archiveUrl}/${forum.icon}`} style={{
                        width: '80px',
                        margin: '20px'
                    }} alt=""></img>
                    <h3>{forum.name}</h3>
                    <p>{forum.introduction}</p>
                </>
            }


            <div className='container' style={{ margin: '20px' }}>
                {isPendingPosts ? (
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

        </div>

        <Link to={'/putpost_forum?forumId=' + forumId} className="link-button" >发帖</Link>
    </div>


}
