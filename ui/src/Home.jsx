import { useEffect } from "react";
import Header from "./components/Header";
import PostList from "./components/PostList";
import { Link } from "react-router-dom";
import './Home.css';


function Home() {
  return (
    <div className="background">
      <Header></Header>
      <div style={{
        width: '100vw',
        height: 'auto',
        display: 'flex',
        justifyContent: 'space-evenly'
      }} id="forumNav">
        <Link to={`/forum?id=1`}>
          <img src={`http://${window.archiveUrl}/anime.jpg`}></img>
        </Link>

        <Link to={`/forum?id=2`}>
          <img src={`http://${window.archiveUrl}/comic.jpg`}></img>
        </Link>

        <Link to={`/forum?id=3`}>
          <img src={`http://${window.archiveUrl}/game.jpg`}></img>
        </Link>

        <Link to={`/forum?id=4`}>
          <img src={`http://${window.archiveUrl}/novel.jpg`}></img>
        </Link>

      </div>
      <PostList></PostList>
      <Link to='putpost' className="link-button">发帖</Link>
    </div>
  );
}

export default Home;
