import React, { useEffect } from 'react';
import './LoginPanel.css'

const SignupPanel = () => {
return <div className='panel'>

        <div className="login_form">
            <form action="#"><br />
                <h1>SIGN UP</h1>
                <input id="lie" type="text" placeholder="user name"/><br /><br />
                <input id="lie" type="password" placeholder="password"/><br /><br />
                <input  id="button"type="submit" value="sign up"/>
            </form>
        </div>
    </div>

};

export default SignupPanel;
