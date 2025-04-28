import * as React from 'react';
import {
    Box,
    FormControl,
    InputLabel,
    TextField,
    Button

} from '@mui/material';
import Stack from '@mui/material/Stack';
import AxiosInstance from '../../api/AxiosInstance';

const LoginForm = () => {
    /*******************State*******************/
    //입력한 회원정보
    const [loginUserData, setLoginUserData] = React.useState({
        "userId": "",
        "passwd": ""
    });


    /******************Communicate****************/
    const doLogin = (e) => {

        if (loginUserData.userId) {

            /*
            * 얕은 복사 : { ...obj} 또는 Object.assign()
            *
            * 깊은 복사 :  structuredClone(), JSON.parse(JSON.stringify(복사할 참조형변수))
            */
            let loginUserParam = JSON.parse(JSON.stringify(loginUserData));

            AxiosInstance.post('/api/doLogin', loginUserParam)
                .then((res) => {
                    console.log(res);
                }

                );

        } else {
            openAlert("id를 입력해주세요");
        }
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

        </Box>
    )

}

export default LoginForm;