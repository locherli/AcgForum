import React, { useEffect, useState } from 'react';
import './LoginPanel.css';
import { Link } from 'react-router-dom';
import { Cookies } from 'react-cookie';

const LoginPanel = () => {
    const cookies = new Cookies();

    const [nameOrEmail, setNameOrEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleLogin = () => {
        console.log("clicked");
        
        const queryBody = {
            userName: nameOrEmail,
            email: nameOrEmail,
            password: password
        };

        fetch('http://localhost:8080/login', {
            method: 'GET',  //it's post actually.
            headers: {
                'Content-Type': 'application/json'
            },
            // body: queryBody
        })
        .then(response => response.json())
        .then(data => {
            if (data.token) {
                // Save data into cookies.
                cookies.set('isLogged', true, { path: '/' });
                cookies.set('userId', data.userId);
                cookies.set('userName', data.userName);
                alert("成功登入");
                window.location.href = '/';
            } else {
                alert("请重新输入");
            }
        })
        .catch(err => {
            console.error(err);
        });
    };

    return (
        <div className='panel'>
            <div className="login_form">
                <form onSubmit={(e) => { e.preventDefault(); handleLogin(); }}>
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
                    <input type="submit" value="log in" onClick={handleLogin} />
                    <p>Have no account yet?</p>
                    <Link to='/signup'>click here</Link>
                </form>
            </div>
        </div>
    );
};

export default LoginPanel;