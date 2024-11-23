import { useEffect, useState } from 'react';
import './Header.css';
import { Link } from 'react-router-dom';
import { Cookies } from "react-cookie";

function Header() {

    const cookies = new Cookies();
    const [isLoged, setIsLoged] = useState(cookies.get('isLogged'));
    const [isPending, setIsPending] = useState(true);
    const [responseBody, setResponseBody] = useState(null);

    useEffect(() => {
        var myHeaders = new Headers();
        myHeaders.append("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
        myHeaders.append("Accept", "*/*");
        myHeaders.append("Host", "apifoxmock.com");
        myHeaders.append("Connection", "keep-alive");

        var requestOptions = {
            method: 'GET',
            headers: myHeaders,
            redirect: 'follow'
        };

        var url = "https://apifoxmock.com/m1/5470794-5146307-default/user/" + cookies.get('userId');

        fetch(url, requestOptions)
            .then(response => response.json())
            .then(result => {
                setResponseBody(result);
                setIsPending(false);
            })
            .catch(error => console.log('error', error));

            console.log(responseBody);
            console.log('url: '+url);
            
            
    }, []);     //Only fired once at first render.

    return (
        <div className='Header'>
            {/* title */}
            <h2>AcgForum</h2>

            {/* search bar */}
            <div className="post-search">
                <input type="text" id="value" placeholder="请输入" />
                <button className="search" title="search">
                    <span>
                        <svg t="1653482794231" className="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2340" width="16" height="16"><path d="M446.112323 177.545051c137.567677 0.219798 252.612525 104.59798 266.162424 241.493333 13.562828 136.895354-78.778182 261.818182-213.617777 289.008485-134.852525 27.203232-268.386263-52.156768-308.945455-183.608889s25.018182-272.252121 151.738182-325.779394A267.235556 267.235556 0 0 1 446.112323 177.545051m0-62.060607c-182.794343 0-330.989899 148.195556-330.989899 330.989899s148.195556 330.989899 330.989899 330.989899 330.989899-148.195556 330.989899-330.989899-148.195556-330.989899-330.989899-330.989899z m431.321212 793.341415a30.849293 30.849293 0 0 1-21.94101-9.102223l-157.220202-157.220202c-11.752727-12.179394-11.584646-31.534545 0.37495-43.50707 11.972525-11.972525 31.327677-12.140606 43.494141-0.37495l157.220202 157.220202a31.036768 31.036768 0 0 1 6.723232 33.810101 31.004444 31.004444 0 0 1-28.651313 19.174142z m0 0" p-id="2341" fill="#ffffff"></path></svg>
                    </span>
                </button>
            </div>

            {/* user avatar */}
            {isLoged && responseBody && !isPending ?
                <a className='userName' href="#用户界面">
                    <img className='avatar' src={responseBody.avatarUrl} />
                    <span>{responseBody.Name}</span>
                </a>
                :
                <Link to='login'>登录</Link>

            }

        </div>
    )
}

export default Header;