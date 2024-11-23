import React, { useEffect, useState } from 'react';
import './LoginPanel.css';
import { Link, useNavigate } from 'react-router-dom';
import { Cookies } from 'react-cookie';

const LoginPanel = () => {
    const cookies = new Cookies();
    const navigate = useNavigate();

    const [nameOrEmail, setNameOrEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = (event) => {
        event.preventDefault(); // 阻止表单的默认提交行为

        console.log("clicked");

        const queryBody = {
            userName: nameOrEmail,
            email: nameOrEmail,
            password: password
        };
        var myHeaders = new Headers();
        myHeaders.append("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
        myHeaders.append("Content-Type", "application/json");
        myHeaders.append("Accept", "*/*");
        myHeaders.append("Host", "apifoxmock.com");
        myHeaders.append("Connection", "keep-alive");

        var requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: JSON.stringify(queryBody), // 确保发送的是 JSON 字符串
            redirect: 'follow'
        };

        fetch("https://apifoxmock.com/m1/5470794-5146307-default/login", requestOptions)
            .then(response => response.json())
            .then(data => {
                if (data.token) {
                    // Save data into cookies.
                    cookies.set('isLogged', true, { path: '/' });
                    cookies.set('userId', data.userId, { path: '/' });
                    cookies.set('userName', data.userName, { path: '/' });
                    alert("成功登入");
                    navigate('/'); // 登录成功后跳转到首页或其他页面
                } else {
                    console.log(data);
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
                        onChange={(e) => setNameOrEmail(e.target.value)}
                        required
                        value={nameOrEmail}
                        type="text"
                        placeholder="username or email"
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
