import React, { useEffect, useState } from "react"
import './PutPostPage.css'
import { Cookies } from "react-cookie";
import { Navigate, useNavigate } from "react-router-dom";


export default function PutPostPage() {
    const cookies = new Cookies();

    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const navi = useNavigate();

    const handleSubmit = () => {

        if (cookies.get('isLogged') === false || cookies.get('isLogged') === undefined) {
            alert('尚未登陆');
            return;
        }

        if (content === '') {
            alert('内容为空！');
            return;
        }
        else if (title === '') {
            alert('标题为空！');
            return;
        }

        console.log(JSON.stringify({
            "title": title,
            "content": content,
            "userId": cookies.get('userId'),
            "isRoot": true,
            "forumId": 0    //means this post doesn't belong to any forum
        }))

        fetch('http://' + window.basicUrl + '/put_post', {
            headers: { 'Content-Type': "application/json" },
            method: 'POST',
            body: JSON.stringify({
                "title": title,
                "content": content,
                "userId": cookies.get('userId'),
                "isRoot": true,
                "forumId": 0    //means this post doesn't belong to any forum
            })
        })
            .catch(err => {
                console.log(err);
            });


        alert('成功，返回主页');
        navi('/');
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