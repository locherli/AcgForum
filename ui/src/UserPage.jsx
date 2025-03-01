import { useEffect, useState } from "react";
import { Cookies } from "react-cookie";
import { Link, useLocation } from "react-router-dom";


export default function UserPage() {
    const id = new URLSearchParams(useLocation().search).get("id");
    const { user, isPending } = useFetchUser(id);
    const cookies = new Cookies();

    function handleSubscribe() {

        var requestOptions = {
            method: 'get',
            redirect: 'follow'
        };

        fetch("http://" + window.basicUrl + "/subscribe_user?myId=" + cookies.get("userId")
            + "&userId=" + id, requestOptions)
            .then(response => response.text())
            .then(result => console.log(result))
            .catch(error => console.log('error', error));
    }

    return (
        <div
            className="backpage"
            style={{
                width: "100%",
                height: "100%",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
            }}
        >
            <div
                className="userPanel"
                style={{
                    backgroundColor: "rgb(0,0,0,0.3)",
                    width: "60vw",
                    height: "auto",
                    borderRadius: "10px",
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    margin: "20px",
                    color: "white",
                }}
            >
                {!isPending && user ? (
                    <>
                        <img
                            className="avatar"
                            src={"defaultAvatar.png"}
                            style={{
                                width: "50px",
                                height: "50px",
                                borderRadius: "3px",
                            }}
                            alt=""
                        />
                        <span>{user.name}</span>
                        <p>{user.selfIntro || "No self introduction yet."}</p>
                        <p>
                            fan number: {user.fanNum} subscribe number:{" "}
                            {user.subscribeNum}
                        </p>
                        {/* SUBSCRIBE BUTTON */}
                        <button onClick={handleSubscribe}

                            // width: 7vw;
                            // height: 5vh;
                            // display: flex;
                            // justify-content: center;
                            // align-items: center;
                            // background-color: rgb(0, 0, 0, 0.32);
                            // position: fixed;
                            // top: 75vh;
                            // right: 10vw;
                            // text-decoration: none;
                            // color: white;
                            // border-radius: 5px;
                            style={{
                                width: "7vw",
                                height: "5vh",
                                margin: "10px",
                                backgroundColor: "rgb(255,255,255, 0.32)",
                                color: "white",
                                borderRadius: "5px",
                                border:"none",
                                cursor:"pointer"
                            }}
                        >关注</button>
                        {/* Render PostList as a component */}
                        <PostList userId={id} />
                    </>
                ) : (
                    <p>Loading...</p>
                )}
            </div>
        </div>
    );
}


function useFetchUser(id) {
    const [user, setUser] = useState(null);
    const [isPending, setIsPending] = useState(true);

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const requestOptions = {
                    method: "GET",
                    headers: { "Content-Type": "application/json" },
                };

                const url = `http://${window.basicUrl}/user/${id}`;
                const response = await fetch(url, requestOptions);
                const data = await response.json();

                setUser(data);
                setIsPending(false);
            } catch (error) {
                console.error("Error fetching user:", error);
                setIsPending(false);
            }
        };

        if (id) {
            fetchUser();
        }
    }, [id]);

    return { user, isPending };
}

function PostList({ userId }) {
    const [isPending, setIsPending] = useState(true);
    const [posts, setPosts] = useState([]);

    useEffect(() => {
        const fetchPosts = async () => {
            try {
                const url = `http://${window.basicUrl}/posts_by_author?authorId=${userId}`;
                const requestOptions = {
                    method: "GET",
                };

                const response = await fetch(url, requestOptions);
                const data = await response.json();

                setPosts(data);
                setIsPending(false);
            } catch (error) {
                console.error("Error fetching posts:", error);
                setIsPending(false);
            }
        };

        if (userId) {
            fetchPosts();
        }
    }, [userId]);

    return (
        <div className="container" style={{ width: "40vw" }}>
            {isPending ? (
                <p>Loading...</p>
            ) : posts.length === 0 ? (
                <p>尚无帖子</p>
            ) : (
                posts.map((post) => (
                    <div className="post" key={post.id}>
                        <Link to={`/detailedpost?id=${post.id}`}>
                            <h4>{post.title}</h4>
                            <p>
                                Likes: {post.like}{" "}
                                {`\u00A0\u00A0\u00A0\u00A0`}Comments:{" "}
                                {post.commentsNum}
                            </p>
                        </Link>
                    </div>
                ))
            )}
        </div>
    );
}

