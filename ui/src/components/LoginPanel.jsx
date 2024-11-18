import React, { useEffect, useState } from 'react';
import './LoginPanel.css'
import { Link } from 'react-router-dom';

const LoginPanel = () => {
    const [nameOrEmail, setNameOrEmail] = useState('');
    const [password, setPassword] = useState('');


    useEffect(() => {
        const button = document.getElementById('button');
        button.addEventListener('click', () => {

        })
    }, []);


    return <div className='panel'>
        <div className="login_form">
            <form action="#"><br />
                <h2 >LOG IN</h2>
                <input onChange={(e) => { setNameOrEmail(e.target.value) }}
                    required value={nameOrEmail} id="lie" type="text" placeholder="username or email" />

                <br /><br />

                <input onChange={(e) => { setPassword(e.target.value) }}
                    required value={password}    id="lie" type="password" placeholder="password" /><br /><br />
                <input id="button" type="submit" value="log in" />
                <p >Have no account yet?</p>
                {/* <a href="#">click here</a> */}
                <Link to='signup'>click here</Link>
            </form>
        </div>


    </div>
};

export default LoginPanel;
