import { useEffect } from "react";
import Header from "./components/Header";
import PostList from "./components/PostList";
import { Link } from "react-router-dom";
import './Home.css';


function Home() {
  return (
    <div className="background">
      <Header></Header>
      <PostList></PostList>
      <Link to='putpost' className="link-button">发帖</Link>
    </div>
  );
}

export default Home;
