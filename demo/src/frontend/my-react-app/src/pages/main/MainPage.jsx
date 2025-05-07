import React from 'react';
import { useState, useEffect } from 'react';
import tokenEncode from '../../commonJs/tokenEncode';
import Alert from '../../components/Alert';



const MainPage = (props) => {

    /*************** STATE **********************/

    //로그인 유저 정보
    const [LoginInfo, setLoginInfo] = useState({
        userId: "",
        userName: ""

    });


    /***************** USE EFFECT ***************************/
    //mount
    useEffect(() => {
        const userInfo = JSON.parse(localStorage.getItem("userInfo"));

        //const token = tokenEncode.getTokenFromCookie('JWTToken');

        if (userInfo) {

            setLoginInfo((prev) => ({
                ...prev,
                userId: userInfo.userId,
                userName: userInfo.userName
            }));

        }

    }, [])


    useEffect(() => {

    }, [LoginInfo])

    const visibleLogin = () => {
        let loginInfo = localStorage.getItem("token");
        console.log("loginInfo::", loginInfo);
    }



    return (

        <h3>메인페이지임</h3>
    );

}

export default MainPage;