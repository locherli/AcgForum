import { useEffect } from "react";
import Header from "./components/Header";
import PostList from "./components/PostList";
import { Link } from "react-router-dom";
import './Home.css'


function Home() {

  const handleClick = () => {
    window.location.href('/putpost');
  };

  useEffect(() => {



  }, []);


  return (
    <div className="background">
      <Header></Header>
      <PostList></PostList>
      <Link href='/putpost' className="link-button">发帖</Link>
    </div>
  );
}

export default Home;
