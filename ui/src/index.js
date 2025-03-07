import React from 'react';
import ReactDOM from 'react-dom/client';
import Home from './Home';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginPage from './LoginPage';
import SignupPage from './SignupPage';
import PutPostPage from './PutPostPage';
import MyselfPage from './MyselfPage'
import DetailedPostPage from './DetailedPostPage';
import UserPage from './UserPage';
import SearchResultPage from './SearchResultPage';
import ForumPage from './ForumPage';
import PutPostPageForum from './PutPostPageForum';
const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
   <div style={{
      margin: '0',
      padding: '0',
      width: '100vw', // 使用vw单位确保宽度是视口的100%
      height: '100vh', // 使用vh单位确保高度是视口的100%
   }}>
      <Router>
         <Routes>
            <Route exact path='/' element={<Home />} />
            <Route path='/login' element={<LoginPage />} />
            <Route path='/signup' element={<SignupPage />} />
            <Route path='/putpost' element={<PutPostPage />} />
            <Route path='/putpost_forum' element={<PutPostPageForum />} />
            <Route path='/myself' element={<MyselfPage />} />
            <Route path='/user' element={<UserPage />} />
            <Route path='/detailedpost' element={<DetailedPostPage />} />
            <Route path='/search' element={<SearchResultPage />} />
            <Route path='/forum' element={<ForumPage />} />
         </Routes>
      </Router>
   </div>

);
