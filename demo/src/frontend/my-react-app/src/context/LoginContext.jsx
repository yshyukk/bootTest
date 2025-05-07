import { createContext, useContext, useState } from 'react';

/*
    # ContextApi
     - 리액트에서 저 ㄴ역 상태를 관리하기 위한 방법 (소, 중규모 프로젝트)
     - props로 부모-자식간 데이터 교환 (props를 계속 전달해야하는 번거로움 -> prop drilling)
     - "공통 데이터"를 상위 컴포넌트에서 한번만 설정하고 하위 컴포넌트 어디서든 쉽게 가져다 씀
    
*/

const AuthContext = createContext(); // 1. Context 생성

export const AuthProvider = ({ children }) => {
    const [loginInfo, setLoginInfo] = useState(() => {
        const storedInfo = localStorage.getItem('userInfo');
        return storedInfo ? JSON.parse(storedInfo) : null;
    });

    const login = (userInfo) => {
        localStorage.setItem('userInfo', JSON.stringify(userInfo));
        setLoginInfo(userInfo);
    };

    const logout = () => {
        localStorage.removeItem('userInfo');
        setLoginInfo(null);
    };

    return (
        <AuthContext.Provider value={{ loginInfo, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);  // 2. Context 사용 hook
