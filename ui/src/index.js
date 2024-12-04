import React from 'react';
import ReactDOM from 'react-dom/client';
import Home from './Home';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginPage from './LoginPage';
import SignupPage from './SignupPage';
import PutPostPage from './PutPostPage';
import UserPage from './UserPage';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
   <Router>
      <Routes>
         <Route exact path='/' element={<Home />} />
         <Route path='/login' element={<LoginPage />} />
         <Route path='/signup' element={<SignupPage />} />
         <Route path='/putpost' element={<PutPostPage />} />
         <Route path='/myself' element={<UserPage />} />
      </Routes>
   </Router>
);
