import React from 'react';
import logo from './logo.svg';
import './App.css';
import { Route, Routes } from 'react-router-dom';
import Layout from './Components/Common/Layout';
import Login from './Components/Login/Login';
import Main from './Components/Main/Main';

function App() {
  return (
    <>
      <Routes>
        <Route element={<Layout/>}>
          <Route path='/login' element={<Login/>}/>
          <Route path='/' element={<Main/>}/>
        </Route>
      </Routes>
    </>
  );
}

export default App;
