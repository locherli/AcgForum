import { useEffect, useState } from "react"
import { redirect, useLocation } from "react-router-dom";
import { Link } from "react-router-dom";

function SearchPost(props) {
    const keyword = props.keyword;

    const [posts, setPosts] = useState([]);
    const [isPending, setIsPending] = useState(true);

    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    useEffect(() => {
        fetch(`http://${window.basicUrl}/search_post?keyword=${keyword}`, requestOptions)
            .then(response => response.json())
            .then(result => {
                setPosts(result);
                setIsPending(false);
            })
            .catch(error => console.log('error', error));

    }, [])

    return (
        <div className='container' style={{ margin: '20px' }}>
            {isPending ? (
                <p>Loading...</p>
            ) : (
                posts.length === 0 ? (
                    <p style={{ color: 'white' }}>没有结果</p>
                ) : (
                    posts.map(post => (
                        <Link to={`/detailedpost/?id=${post.id}`} key={post.id} style={{ marginTop: '10px' }}>
                            <div className='post'>
                                <h4>{post.title}</h4>
                                <p>Likes: {post.like} {`\u00A0\u00A0\u00A0\u00A0`} Comments: {post.commentsNum}</p>
                            </div>
                        </Link>
                    ))
                )
            )}
        </div>
    );
}

function SearchUser(props) {
    const keyword = props.keyword;
    const [isPending, setIsPending] = useState(true);
    const [users, setUsers] = useState([]);

    const requestOptions = {
        method: 'GET',
        redirect: 'follow'
    }

    useEffect(() => {
        fetch(`http://${window.basicUrl}/search_user?keyword=${keyword}`, requestOptions)
            .then(response => response.json())
            .then(response => {
                setUsers(response);
                console.log(response)
                setIsPending(false);
            }).catch(err => { console.log(err) });
    }, [])

    return <div className='container' style={{ margin: '20px' }}>
        {isPending ? (
            <p>Loading...</p>
        ) : (
            users.length === 0 ? (
                <p style={{ color: 'white' }}>没有结果</p>
            ) : (
                users.map(user => (
                    <Link to={`/user?id=${user.id}`} key={user.id} style={{ marginTop: '10px' }}>
                        <h4>{user.userName}</h4>
                    </Link>
                ))
            )
        )}
    </div>
}


export default function SearchResultPage() {
    const location = useLocation();
    const searchParams = new URLSearchParams(location.search);
    const keyword = searchParams.get('keyword');
    const [navState, setNavState] = useState(0);
    //0: means search post.
    //1: means search user.

    function renderSearchResult() {
        switch (navState) {
            case 0: return <SearchPost keyword={keyword}></SearchPost>
            case 1: return <SearchUser keyword={keyword}></SearchUser>
            default: return <p>error! pls refresh the page.</p>
        }

    }


    return <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
        {/* navigation bar */}
        <ul style={{ display: 'flex', listStyle: 'none' }} id="nav">
            <li>
                <button onClick={() => { setNavState(0) }}>
                    帖子
                </button>
            </li>
            <li>
                <button onClick={() => { setNavState(1) }}>
                    用户
                </button>
            </li>
        </ul>
        <div style={{
            height: 'auto',
            width: '80vw',
            backgroundColor: "rgba(0,0,0,0.25)",
            display: 'flex'
        }}>
            {renderSearchResult()}
        </div>
    </div>
}