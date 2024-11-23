import React, { useEffect, useState } from "react"
import './PutPostPage.css'
import { Cookies } from "react-cookie";


export default function PutPostPage() {
    const cookies = new Cookies();

    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');

    const handleSubmit = () => {

        fetch('http://localhost:8080/put-post', {
            method: 'POST',
            body: {
                "title": title,
                "content": content,
                "userId": cookies.get('userId')
            }
        }).catch(err => {
            console.log(err);
        })



    }

    return <div style={
        {
            width: '100%',
            height: '100%',
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center'
        }
    }>
        <div className="panel">
            <textarea id='title' onChange={(e) => { setTitle(e.target.value) }}>
                输入标题
            </textarea>
            <textarea id='content' rows="40" cols="60" onChange={(e) => { setContent(e.target.value) }}>
                输入内容
            </textarea>
            <button onClick={handleSubmit}>提交</button>
        </div>
    </div>
}