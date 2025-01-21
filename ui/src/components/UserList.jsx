import { useEffect, useState } from "react";
import { Cookies } from "react-cookie";
import { Link } from "react-router-dom";

export function SubscribedUserList() {
    const cookie = new Cookies();
    const [isPending, setIsPending] = useState(true);
    const [users, setUsers] = useState([]);
    const [page, setPage] = useState(1);

    useEffect(() => {
        const cookie = new Cookies();
        fetch('http://' + window.basicUrl + '/subscribed_users?id=' + cookie.get('userId') + '&page=' + page, {
            headers: { 'Content-Type': 'application/json' },
            method: 'GET',
        }).then(data => data.json())
            .then(data => { setUsers(data); setIsPending(false); })
            .catch(err => {
                console.log(err);
            });

    }, []);

    function handleUnsubscribe(id) {
        fetch(`http://${window.basicUrl}/unsubscribe_user?myId=${cookie.get('userId')}&userId=${id}`)
            .catch(err => { alert('网络异常！'); return; });
        alert('成功取关');
        window.location.reload();
    }


    return (
        <div className='container'
            style={{
                width: '40vw',
                margin: '10px',
                padding: '10px'
            }}
        >
            {isPending ? (
                <p>Loading...</p>
            ) : (
                Array.isArray(users) && users.length > 0 ? (
                    users.map(user => (
                        <div key={user.id} style={{ display: 'flex', flexDirection: 'row' }}>
                            <Link to={'/user?id=' + user.id} style={{ textDecoration: 'none' }}>
                                <h4 >{user.userName}</h4>
                            </Link>
                            <button onClick={() => handleUnsubscribe(user.id)}
                                style={{
                                    width: '60px',
                                    height: '30px',
                                    position: 'absolute',
                                    right: '30px',
                                    marginTop: '10px',
                                    backgroundColor: 'rgba(255,255,255,0.5)',
                                    border: 'none',
                                    borderRadius: '10px',
                                    cursor: 'pointer'
                                }}>取关</button>
                        </div>
                    ))

                ) : (
                    <p>你未关注任何用户</p>
                )
            )}
        </div>
    );
}

export function FanList() {
    const [isPending, setIsPending] = useState(true);
    const [users, setUsers] = useState([]);

    useEffect(() => {
        const cookie = new Cookies();
        fetch('http://' + window.basicUrl + '/fans/' + cookie.get('userId'), {
            headers: { 'Content-Type': 'application/json' },
            method: 'GET',
        })
            .then(data => data.json())
            .then(data => { setUsers(data); setIsPending(false); })
            .catch(err => {
                console.log(err);
            });
    }, [])


    return (
        <div className='container'
            style={{
                width: '40vw',
                margin: '10px',
                padding: '10px'
            }}
        >
            {isPending ? (
                <p>Loading...</p>
            ) : (
                Array.isArray(users) && users.length > 0 ? (
                    users.map(user => (
                        <Link to={`/user?id=${user.id}`} key={user.id}>
                            <div>
                                <h4>{user.userName}</h4>
                            </div>
                        </Link>
                    ))
                ) : (
                    <p>无用户关注你</p>
                )
            )}
        </div>
    );


}