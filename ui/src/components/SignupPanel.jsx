import React, { useEffect, useState } from 'react';
import './LoginPanel.css'
import { Navigate, useNavigate } from 'react-router-dom';

const SignupPanel = () => {

    const [email, setEmail] = useState('');
    const [userName, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const navigate = new useNavigate();


    const handleSignup = (event) => {
        event.preventDefault(); //prevent default action after submit the form.

        var raw = JSON.stringify({
            "userName": userName,
            "email": email,
            "password": password
        });

        var requestOptions = {
            headers: { 'Content-Type': 'application/json' },
            method: 'POST',
            body: raw,
            redirect: 'follow'
        };

        //example of response
        //{
        //     "isLegalEmail": true,
        //     "isLegalUserName": true
        // }

        fetch('http://' + window.basicUrl + '/signup', requestOptions)
            .then(response => response.json())
            .then(result => {
                if (!result.isLegalEmail)
                    alert('非法的邮箱，请重新输入！');
                else if (!result.isLegalUserName)
                    alert('非法的用户名，请重新输入！');
                else {
                    alert('注册成功，请登录');
                    navigate('/login');
                }
            })
            .catch(error => console.log('error', error));
    };



    return <div className='log_panel'>

        <div className="login_form">
            <form onSubmit={handleSignup}>
                <br />
                <h1>SIGN UP</h1>
                <input type="text" placeholder="email"
                    onChange={(e) => { setEmail(e.target.value) }} /><br /><br />

                <input type="text" placeholder="user name"
                    onChange={(e) => { setUserName(e.target.value) }} /><br /><br />

                <input type="password" placeholder="password"
                    onChange={(e) => { setPassword(e.target.value) }} /><br /><br />
                <input id="button" type="submit" value="sign up" onClick={handleSignup} />
            </form>
        </div>
    </div>

};

export default SignupPanel;
