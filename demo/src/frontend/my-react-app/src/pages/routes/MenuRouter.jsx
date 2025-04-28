import React from 'react';
import { Routes, Route } from 'react-router-dom';
import MainPage from '../main/MainPage';
import RegisterUserPage from '../user/RegisterUserPage';
import BoardList from '../board/BoardList';
import LoginForm from '../main/LoginForm';

const MenuRouter = () => {
    return (
        <Routes>
            <Route path="/" element={<MainPage />} />
            <Route path="/RegisterUser" element={<RegisterUserPage />} />
            <Route path="/BoardListPage" element={<BoardList />} />
            <Route path="/LoginForm" element={<LoginForm />} />
        </Routes>

    )
}

export default MenuRouter;