import React from 'react';
import ReactDOM from 'react-dom/client';
import Home from './Home';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import LoginPage from './LoginPage';
import SignupPage from './SignupPage';
import PutPostPage from './PutPostPage';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
   <Router>
      <Routes>
         <Route path='/' element={<Home/>} />
         <Route path='/login' element={<LoginPage/>} />
         <Route path='/signup' element={<SignupPage/>} />
         <Route path='/putpost' element={<PutPostPage/>} />
      </Routes>
   </Router>
);
