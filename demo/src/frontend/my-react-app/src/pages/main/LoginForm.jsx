import React, { useState, useContext, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import {
    Box,
    FormControl,
    InputLabel,
    TextField,
    Button

} from '@mui/material';
import Stack from '@mui/material/Stack';
import AxiosInstance from '../../api/AxiosInstance';
import Confirm from '../../components/Confirm';
import Alert from '../../components/Alert';
import { Link } from "react-router-dom";
import { useAuth } from '../../context/LoginContext';

const LoginForm = () => {
    /*******************State*******************/
    const { login } = useAuth();

    //입력한 회원정보
    const [loginUserData, setLoginUserData] = React.useState({
        "userId": "",
        "userName": ""
    });

    const navigate = useNavigate();

    const [openConf, setOpenConf] = React.useState(false);
    //confrim 확인 후 callback함수
    const [confAction, setConfAction] = React.useState(() => () => { });

    //Alert
    const [alertOpen, setAlertOpen] = useState(false);
    const [alertMsg, setAlertMsg] = useState('');

    /******************Communicate****************/
    const doLogin = (e) => {

        if (loginUserData.userId) {

            /*
            * 얕은 복사 : { ...obj} 또는 Object.assign()
            *
            * 깊은 복사 :  structuredClone(), JSON.parse(JSON.stringify(복사할 참조형변수))
            */
            let loginUserParam = JSON.parse(JSON.stringify(loginUserData));

            AxiosInstance.post('/api/doLogin', loginUserParam, {

            })
                .then((res) => {
                    console.log(res);
                    //로그인 성공 -> 토큰을 localStorage에 저장

                    let loginUserInfo = res.data;

                    localStorage.setItem("userInfo", JSON.stringify(loginUserInfo));

                    //context에 로그인 정보 저장
                    login(loginUserInfo);

                    //메인 페이지로 리다이렉션
                    navigate("/");
                })
                .catch((err) => {

                    if (err.response && err.response.data && err.response.status == "401") {
                        openAlert(err.response.data.errorMsg);
                        return;
                    }
                });

        } else {
            openAlert("id를 입력해주세요");
        }
    }
    /***************** Comm Fucntion *****************/

    const openAlert = (msg) => {
        setAlertMsg(msg);
        setAlertOpen(true);
    };

    const openConfirm = (e, callback) => {

        const cpSaveParam = { ...formData };
        //필수값 체크
        if (chkReqValidForm(e, cpSaveParam, userForm) === false) {
            return;
        }

        setConfAction(() => () => callback(e));
        setOpenConf(true);

    }

    const handelConf = () => {
        confAction(); // state에 저장된 함수 실행
        setOpenConf(false);
    }


    /********************FUNCTION*******************************/

    const inputChage = (e) => {

        const { name, value } = e.target;

        setLoginUserData((data) => ({ ...data, [name]: value }));

    }
    return (

        <Box sx={{ display: 'flex', justifyContent: 'center', marginTop: '50px', width: '100%' }}>

            <Stack direction={"column"} sx={{ width: '40%' }}>
                <FormControl>
                    <TextField
                        id='outlined-error-helper-text'
                        name='userId'
                        placeholder='ID'
                        value={loginUserData.userId}
                        onChange={(e) => inputChage(e)}

                    >
                    </TextField>
                    <TextField
                        id='passwd'
                        name='passwd'
                        placeholder='PASSWORD'
                        type='password'
                        value={loginUserData.passwd}
                        sx={{ marginTop: '10px' }}
                        onChange={(e) => inputChage(e)}
                    >
                    </TextField>
                </FormControl>
                <FormControl sx={{ display: 'flex', justifyContent: 'center' }}>
                    <Box>
                        <Button
                            variant='contained'
                            sx={{
                                width: '100px'
                                , marginTop: '10px'
                            }}
                            onClick={(e) => doLogin(e)}
                        > 로그인
                        </Button>
                    </Box>
                </FormControl>

                <FormControl>
                    <Stack direction={"row"} justifyContent={'space-between'} sx={{ marginTop: '10px' }}>
                        <Button variant='text'> 회원가입 </Button>
                        <Button variant='text'> 아이디 찾기 </Button>
                        <Button variant='text'> 비밀번호 찾기 </Button>
                    </Stack>
                </FormControl>

            </Stack>


            <Confirm
                open={openConf}
                onClose={() => setOpenConf(false)}
                onConfirm={handelConf}
            />
            <Alert
                open={alertOpen}
                message={alertMsg}
                onClose={() => setAlertOpen(false)}
            />

        </Box>
    )

}

export default LoginForm;