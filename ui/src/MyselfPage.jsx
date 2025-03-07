import { useEffect, useState } from "react";
import { Cookies } from "react-cookie";
import { useNavigate } from "react-router-dom";
import './MyselfPage.css'
import { CollectedPostList, MyPostList } from "./components/PostList";
import { FanList, SubscribedUserList } from "./components/UserList";

export default function MyselfPage() {
    const cookies = new Cookies();
    const [isPending, setIsPending] = useState(true);
    const [responseBody, setResponseBody] = useState(null);
    const navi = useNavigate();
    const [navState, setNavState] = useState(0);

    const [showTextBar, setShowTextBar] = useState(false);
    const [textBar, setTextBar] = useState('');

    useEffect(() => {
        const fetchUser = async () => {
            var requestOptions = {
                method: 'GET',
                headers: { 'Content-Type': 'application/json' },
                redirect: 'follow'
            };

            var url = "http://" + window.basicUrl + "/user/" + cookies.get('userId');

            try {
                const response = await fetch(url, requestOptions);
                const data = await response.json();
                setResponseBody(data);
                setIsPending(false);
            } catch (error) {
                console.log('error', error);
            }
        };

        fetchUser();
    }, []);


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
            default: return <p>error! please refresh the page.</p>
        }
    }

    function handleChangeAvatar() {
        const fileInput = document.createElement('input');
        fileInput.type = 'file';
        fileInput.accept = 'image/*';

        fileInput.addEventListener('change', (e) => {
            const file = e.target.files[0];
            if (!file) return;

            const formData = new FormData();
            formData.append('avatarFile', file);

            const requestOptions = {
                method: 'POST',
                body: formData,
                // 不要设置 Content-Type，浏览器会自动处理
                redirect: 'follow'
            };

            fetch(`http://${window.basicUrl}/change_avatar/${cookies.get("userId")}`, requestOptions)
                .then(response => response.text())
                .then(console.log)
                .catch(console.error);
        });

        fileInput.click();
    }

    function handleSubmit(event) {
        event.preventDefault();


        var raw = JSON.stringify({
            "userId": cookies.get('userId'),
            "newIntro": textBar
        });

        var requestOptions = {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: raw,
            redirect: 'follow'
        };

        fetch(`http://${window.basicUrl}/alter_user_intro`, requestOptions)
            .then(response => response.text())
            .then(result => console.log(result))
            .catch(error => console.log('error', error));

        window.location.reload();
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
                <img className='avatar' src={`http://${window.archiveUrl}/${responseBody.avatarUrl}`}
                    onClick={handleChangeAvatar}
                    style={{
                        width: '50px',
                        height: '50px',
                        borderRadius: '3px',
                        margin: '10px'
                    }}
                    alt=""
                />
                <span>{responseBody.name}</span>
                {/* <p>{JSON.stringify(responseBody)}</p> */}

                <p onClick={() => setShowTextBar(!showTextBar)} style={{ cursor: 'pointer' }}>
                    {responseBody.selfIntro ? responseBody.selfIntro : "No self introduction yet."}
                </p>

                {showTextBar ?
                    <form onSubmit={handleSubmit}>
                        <input type="text" placeholder="输入你的个性签名" value={textBar} onChange={(e) => setTextBar(e.target.value)}></input>
                        <input type="submit" style={{ margin: '10px' }}></input>
                    </form> : <></>}

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
