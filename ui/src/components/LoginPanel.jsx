import React, { useEffect, useState } from 'react';
import './LoginPanel.css';
import { Link, useNavigate } from 'react-router-dom';
import { Cookies } from 'react-cookie';

const LoginPanel = () => {
    const cookies = new Cookies();
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = (event) => {
        event.preventDefault(); // 阻止表单的默认提交行为

        const queryBody = {
            "email": email,
            "password": password
        };

        var requestOptions = {
            headers:{'Content-Type':'application/json'},
            method: 'POST',
            body: JSON.stringify(queryBody), // 确保发送的是 JSON 字符串
            redirect: 'follow'
        };

        var url='http://'+window.basicUrl+ '/login';

        //response body belike:
        // {
        //     "token": true,
        //     "id": 4,
        //     "name": "locher",
        //     "avatarUrl": null,
        //     "selfIntro": null,
        //     "fanNum": 0,
        //     "subscribeNum": 0
        // }
        fetch(url, requestOptions)
            .then(response => response.json())
            .then(data => {

                console.log(data);
                

                if (data.token) {
                    // Save data into cookies.
                    cookies.set('isLogged', true, { path: '/' });
                    cookies.set('userId', data.id, { path: '/' });
                    cookies.set('userName', data.name, { path: '/' });
                    alert("成功登入");
                    navigate('/'); // 登录成功后跳转到首页或其他页面
                } else {
                    alert("请重新输入");
                }
            })
            .catch(error => console.log('error', error));
    };

    return (
        <div className='log_panel'>
            <div className="login_form">
                <form onSubmit={handleLogin}>
                    <h2>LOG IN</h2>
                    <input
                        onChange={(e) => setEmail(e.target.value)}
                        required
                        value={email}
                        type="text"
                        placeholder="email"
                    />
                    <br /><br />
                    <input
                        onChange={(e) => setPassword(e.target.value)}
                        required
                        value={password}
                        type="password"
                        placeholder="password"
                    />
                    <br /><br />
                    <input type="submit" value="log in" />
                    <p>Have no account yet?</p>
                    <Link to='/signup'>click here</Link>
                </form>
            </div>
        </div>
    );
};

export default LoginPanel;
