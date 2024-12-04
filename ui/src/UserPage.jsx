import { useState } from "react";
import { Cookies } from "react-cookie";



export default function UserPage() {

    const cookies = new Cookies();
    const [isPending, setIsPending] = useState(true);
    const responseBody = useFetchUser(cookies.get('userId'));


    return <div style={{
        width: '100%',
        height: '100%',
        display: 'flex',
        justifyContent: 'center'
    }}>
        <div style={{
            width: '50vw',
            height: 'auto',
            backgroundColor: 'rgb(0,0,0,0.3)',
            backdropFilter: 'blur(2px)',
            borderRadius: '10px',
            display: 'flex',
            justifyContent: 'center'
        }}>
            {isPending && responseBody ? <>
                <img className='avatar' src={responseBody.avatarUrl} />
                <span>{responseBody.Name}</span>
            </> : <p>loading...</p>}

        </div>
    </div>
}


function useFetchUser(id) {

    const [responseBody, setResponseBody] = useState(null);

    var myHeaders = new Headers();
    myHeaders.append("User-Agent", "Chrome");
    myHeaders.append("Accept", "*/*");
    myHeaders.append("Host", "127.0.0.1:4523");
    myHeaders.append("Connection", "keep-alive");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    var url = "http://127.0.0.1:4523/m1/5470794-5146307-default/user/" + id;

    fetch(url, requestOptions)
        .then(response => response.json())
        .then(result => console.log(result))
        .then(response => { setResponseBody(response) })
        .catch(error => console.log('error', error));
    return responseBody;
};