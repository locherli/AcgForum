import Header from "./components/Header";
import { createContext} from "react";
import PostList from "./components/PostList";
import './Home.css'




function Home() {
  return (
    <div className="background">
      <Header></Header>
      <PostList></PostList>

    </div>
  );
}

export default Home;
