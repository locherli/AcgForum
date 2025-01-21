import { useState } from "react";
import { Cookies } from "react-cookie";
import { useNavigate } from "react-router-dom";
import './MyselfPage.css'
import { CollectedPostList, MyPostList} from "./components/PostList";
import { FanList, SubscribedUserList } from "./components/UserList";

export default function MyselfPage() {

    const cookies = new Cookies();
    const [isPending, setIsPending] = useState(true);
    const responseBody = useFetchUser(cookies.get('userId'));
    const navi = useNavigate();
    const [navState, setNavState] = useState(0);
    //0 means collected post
    //1 means my posts
    //2 means the users you subscribed
    //3 means the users subscribed you

    function handleLogout() {
        cookies.remove('userName');
        cookies.remove('userId');
        cookies.set('isLogged', false, { path: false });
        navi('/');
    };

    function useFetchUser(id) {

        const [responseBody, setResponseBody] = useState(null);

        var requestOptions = {
            method: 'GET',
            headers: { 'Content-Type': 'application/json' },
            redirect: 'follow'
        };

        var url = "http://" + window.basicUrl + "/user/" + id;

        fetch(url, requestOptions)
            .then(response => response.json())
            .then(response => {
                setIsPending(false);
                setResponseBody(response);
            })
            .catch(error => console.log('error', error));

        return responseBody;
    };

    function renderList() {
        switch (navState) {
            case 0:
                return <CollectedPostList></CollectedPostList>
            case 1:
                return <MyPostList></MyPostList>
            case 2:
                return <SubscribedUserList></SubscribedUserList>
            case 3:
                return <FanList></FanList>
            default:
        }
    }


    return <div style={{
        width: '100%',
        height: '100%',
        display: 'flex',
        justifyContent: 'center',
        color: 'white'
    }}>
        <div className="panel" style={{
            width: '50vw',
            height: 'fit-content',
            backgroundColor: 'rgb(0,0,0,0.3)',
            backdropFilter: 'blur(2px)',
            borderRadius: '10px',
            display: 'flex',
            flexDirection: 'column', // 修改为列布局

            alignItems: 'center' // 确保内容在垂直方向上居中

        }}>
            {/* /www/wwwroot/resource/defaultAvatar.png */}
            {!isPending && responseBody ? <>
                <img className='avatar' src={'defaultAvatar.png'}
                    style={{
                        width: '50px',
                        height: '50px',
                        borderRadius: '3px'
                    }}
                    alt=""
                />
                <span>{responseBody.name}</span>
                {/* <p>{JSON.stringify(responseBody)}</p> */}

                <p>{responseBody.selfIntro ? responseBody.selfIntro : "No self introduction yet."}</p>
                <p>fan number: {responseBody.fanNum} subscribe number: {responseBody.subscribeNum}</p>
            </> : <p>Loading...</p>}


            {/* navigation bar */}
            <ul style={{ display: 'flex', listStyle: 'none' }} id="nav">
                <li>
                    <button onClick={() => { setNavState(0) }}>
                        收藏的帖子
                    </button>
                </li>
                <li>
                    <button onClick={() => { setNavState(1) }}>
                        我的帖子
                    </button>
                </li>
                <li>
                    <button onClick={() => { setNavState(2) }}>
                        关注的用户
                    </button>
                </li>
                <li>
                    <button onClick={() => { setNavState(3) }}>
                        关注了你的用户
                    </button>
                </li>
            </ul>


            {renderList()}

            <button onClick={handleLogout}
                style={{
                    backgroundColor: 'rgb(255,255,255,0.3)',
                    border: 'none',
                    width: '50px',
                    height: '30px',
                    borderRadius: '5px',
                    margin: '10px',

                }}
            >登出</button>
        </div>


    </div>
}
