import { useEffect, useRef, useState } from "react"
import { redirect, useLocation, useParams, Link, useNavigate } from "react-router-dom";
import './imgs/karen.jpeg';
import './DetailedPostPage.css';
import { Cookies } from "react-cookie";

export default function DetailedPostPage() {

    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const id = searchParams.get('id');
    const cookies = new Cookies();
    const [post, setPost] = useState(null);
    const [isPending, setIsPending] = useState(true);
    const [isLiked, setIsLiked] = useState(false);
    const [isCollected, setIsCollected] = useState(false);
    const [textArea, setTextArea] = useState('');
    const [comments, setComments] = useState([]);
    const navi = useNavigate();

    useEffect(() => {

        let url = 'http://' + window.basicUrl + '/post/' + id;
        let requestOptions = {
            headers: { 'Content-Type': 'application/json' },
            method: 'GET',
            redirect: 'follow'
        };

        fetch(url, requestOptions)
            .then(data => data.json())
            .then(data => {
                setPost(data);

                if (data.commentsId != null) {
                    Promise.all(data.commentsId.map(i => fetchComment(i)))
                        .then(() => setIsPending(false));
                } else {
                    setIsPending(false); // If no comments, stop loading
                }
            })
            .catch(err => {
                console.log(err);
                setIsPending(false); // Stop loading on error
            });
    }, []);

    function fetchComment(postId) {
        return fetch('http://' + window.basicUrl + '/post/' + postId)
            .then(data => data.json())
            .then(newComment => {
                setComments(originComment => [...originComment, newComment]);
            })
            .catch(err => {
                console.log(err);
            });
    }

    function handleLike() {
        if (cookies.get('isLogged') === undefined || cookies.get('isLogged') === false) {
            alert('请先登录');
            return;
        }

        const url = 'http://' + window.basicUrl + '/like_post?userId=' + cookies.get('userId') + '&postId=' + id;
        fetch(url, {
            headers: { 'Content-Type': 'application/json' },
            redirect: 'follow',
        }).then(data => data.json())
            .then(data => {
                setIsLiked(!data.isOk);

                if (!data.isOk === true) {
                    alert('you already liked!');
                    return;
                }

                setIsLiked(true);
            })
            .catch(err => { console.log(err); })

    }

    function handleCollect() {
        if (cookies.get('isLogged') === undefined || cookies.get('isLogged') === false) {
            alert('请先登录');
            return;
        }
        if (isCollected === true) {
            alert('you already collected this post!');
            return;
        }

        const url = 'http://' + window.basicUrl + '/collect_post?userId=' + cookies.get('userId') + '&postId=' + id;

        fetch(url, {
            headers: { 'Content-Type': 'application/json' },
            redirect: 'follow'
        })
            .then(data => data.json())
            .then(data => {
                if (!data.isOk) {
                    alert('you already collected this post!');
                    return;
                }
                setIsCollected(true);
            })
            .catch(err => { console.log(err); alert('网络异常！'); return; })

        setIsCollected(true);
    }

    function handleComment() {

        if (textArea === '') {
            alert('内容为空！');
            return;
        }

        const url = 'http://' + window.basicUrl + '/put_post';

        fetch(url, {
            headers: { 'Content-Type': 'application/json' },
            method: 'POST',
            redirect: 'follow',
            body: JSON.stringify({
                title: null,
                content: textArea,
                userId: cookies.get('userId'),
                isRoot: false,
                root: id
            })
        }).catch(err => { console.log(err) });

        alert('提交成功');
        setTextArea('');
    }

    return <div className="backImg">

        <div className="DetailedPage"
            style={{
                width: '100%',
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                color: 'white'
            }}
        >
            <div className="postPanel"
                style={{
                    width: '70vw',
                    height: 'auto',
                    backgroundColor: 'rgb(0,0,0,0.3)',
                    backdropFilter: 'blur(2px)',
                    borderRadius: '10px',
                    display: 'flex',
                    flexDirection: 'column', // 修改为列布局
                    justifyContent: 'center',
                    alignItems: 'center', // 确保内容在垂直方向上居中
                    marginTop: "20px"
                }}
            >

                {isPending ?
                    <p>Loading...</p>
                    :
                    <>

                        {post.title ? <h2>{post.title}</h2> : <></>}

                        {post.authorName ?
                            <p>
                                <Link to={`/user?id=${post.authorId}`}
                                    style={{ textDecoration: 'none', color: 'white' }}>作者:{post.authorName} </Link>
                                {post.date} {post.like} commentNum: {post.commentsNum}
                            </p> : <></>}


                        <p style={{ whiteSpace: 'pre-wrap' }} >{post.content}</p>

                        <div className="actionBar"
                            style={{ display: 'flex', }}>

                            {/* like button */}
                            <div onClick={handleLike}
                                style={{
                                    display: 'inline-block',
                                    cursor: 'pointer',
                                    margin: '10px'
                                }}>
                                <img src="/like.jpg" alt="like"
                                    style={{
                                        width: '30px',
                                        height: '30px',
                                        borderRadius: '100%',
                                    }}
                                />
                            </div>

                            {/* collect button */}
                            <div onClick={handleCollect}
                                style={{
                                    display: 'inline-block',
                                    cursor: 'pointer',
                                    margin: '10px'
                                }}>
                                <img src="/collect.jpg" alt="collectIcon"
                                    style={{
                                        width: '30px',
                                        height: '30px',
                                        borderRadius: '100%',
                                    }}
                                />
                            </div>
                        </div>

                        <div className="textAreaBar"
                            style={{ display: 'flex', }}>
                            <textarea id='textArea'
                                onChange={(e) => { setTextArea(e.target.value) }}
                                style={{
                                    backgroundColor: 'rgba(255, 255, 255, 0.5)',
                                    width: '40vw',
                                    margin: '20px'
                                }}
                                value={textArea}
                                placeholder="万水千山总是情，留点评论行不行" />
                            <button style={{
                                backgroundColor: 'rgba(255, 255, 255, 0.5)',
                                height: '60px',
                                marginRight: '10px'
                            }} onClick={handleComment}>提交</button>
                        </div>

                        {
                            comments.map(i => (
                                <div key={i.id}
                                    onClick={() => { window.open(`/detailedpost?id=${i.id}`, '_blank'); }}
                                    style={{
                                        width: '500px',
                                        height: 'auto',
                                        margin: '10px',
                                        padding: '5px',
                                        cursor: 'pointer'
                                    }}
                                >
                                    <p>{i.content}</p>
                                    <p style={{ fontSize: '10px' }}>
                                        {i.authorName} {i.date} likeNum: {i.like} commentNum: {i.commentsNum}
                                    </p>
                                </div>
                            ))
                        }


                    </>

                }




            </div>
        </div>
    </div>
}





