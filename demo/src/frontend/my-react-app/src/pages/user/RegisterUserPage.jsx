import React, { useState, useEffect, useRef } from 'react';
import {
    Box,
    FormControl,
    Input,
    InputLabel,
    Button

} from '@mui/material';
import IconButton from '@mui/material/IconButton';
import InputAdornment from '@mui/material/InputAdornment';
import Visibility from '@mui/icons-material/Visibility';
import VisibilityOff from '@mui/icons-material/VisibilityOff';
import AxiosInstance from '../../api/AxiosInstance';
import Stack from '@mui/material/Stack';
import DaumAddressApi from '../../api/DaumAddressApi';
import Confirm from '../../components/Confirm';
import Alert from '../../components/Alert';


const RegisterUserPage = (props) => {

    /***************** STATE *****************/
    const [formData, setFormData] = useState({
        userName: '',
        birth: '',
        userId: '',
        passwd: '',
        passwdConf: '',
        phoneNum: '',
        email: '',
        address: '',
        dtlAddress: ''
    });

    const [formDataPassYn, setFormDataPassYn] = React.useState({
        userNameYn: "N",
        userIdYn: "N",

    });

    const [showPassword, setShowPassword] = React.useState(false);
    const [showPasswordConf, setShowPasswordConf] = React.useState(false);
    const [openConf, setOpenConf] = React.useState(false);
    //confrim 확인 후 callback함수
    const [confAction, setConfAction] = React.useState(() => () => { });

    //Alert
    const [alertOpen, setAlertOpen] = useState(false);
    const [alertMsg, setAlertMsg] = useState('');

    //이름 입력 조합중 상태 감지
    const [isComposing, setIsComposing] = useState(false);

    const handleCompositionStart = () => setIsComposing(true);
    const handleCompositionEnd = () => setIsComposing(false);

    //아이디 중복체크
    const [idDuplicateChk, setIdDuplicateChk] = useState("");

    /****************** REF *************************/

    const userForm = useRef(null);

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

    /*****USE EFFECT ******/

    useEffect(() => {

        if (!idDuplicateChk) return; // 초기값 무시

        if (idDuplicateChk === "Y") {
            openAlert('사용할수 없는 아이디입니다.');
            setFormData((prev) => ({ ...prev, "userId": "" }));
            setFormDataPassYn((prev) => ({ ...prev, "userIdYn": "N" }))
            return;
        } else if (idDuplicateChk === "N") {
            setFormDataPassYn((prev) => ({ ...prev, "userIdYn": "Y" }))
            openAlert('사용가능한 아이디입니다.');
        }
    }, [idDuplicateChk])


    /**************** COMMUNICATE *******************/
    const postUserData = () => {
        AxiosInstance.post('/api/userRegister', formData)
            .then((res) => console.log(res.data));

    }

    const getIdDuplicateChk = () => {
        let chkParam = {};

        if (formData.userId) {
            chkParam["chkId"] = formData.userId;

            AxiosInstance.get('/api/getIdDuplicateChk', { params: chkParam })
                .then((res) => {
                    setIdDuplicateChk(res.data.CHKYN)
                    console.log("res::", res.data.CHKYN)

                }

                );
        } else {
            openAlert("id를 입력해주세요");
        }
    }

    /************** function *****************/

    const inputBlur = (e) => {

        const { name, value } = e.target;
        if (value) {

            chkInputLength(e);

            if (name == "userName") {

                let chkName = value.trim().replace(/\s/g, "");

                // 조합 중이면 더 이상의 검사는 하지 않음
                if (isComposing) return;

                // 한글, 영어 구분
                const isOnlyKorean = /^[가-힣]+$/.test(chkName);
                const isOnlyEnglish = /^[a-zA-Z]+$/.test(chkName);

                if (!isOnlyKorean && !isOnlyEnglish) {
                    openAlert("한글과 영어만 입력가능합니다. \n (한글과 영어의 조합은 불가)");
                    return;
                }

                if (isOnlyKorean && (chkName.length < 2 || chkName.length > 20)) {

                    if (chkName.length > 20) {

                        chkName = chkName.slice(0, 20);
                        setFormData((prev) => ({ ...prev, [name]: chkName }));
                    }

                    openAlert("이름은 한글 2자 이상 20자 이하로 입력해주세요.");
                    return;
                }

                if (isOnlyEnglish && (chkName.length < 3 || chkName.length > 20)) {

                    if (chkName.length > 20) {

                        chkName = chkName.slice(0, 20);
                        setFormData((prev) => ({ ...prev, [name]: chkName }));
                    }
                    openAlert("이름은 영어 3자 이상 20자 이하로 입력해주세요.");
                    return;

                }
                // 유효한 경우에만 값 저장
                setFormData((prev) => ({ ...prev, [name]: chkName }));
                setFormDataPassYn((prev) => ({ ...prev, "userNameYn": "Y" }));

            } else if (name === "userId") {

                let isValidId = /^[a-zA-Z0-9]+$/.test(value);

                if (!isValidId) {
                    openAlert("아이디는 영어와 숫자만 입력 가능합니다.");

                    return;
                }

                if (isValidId.length < 4 || isValidId.length > 30) {
                    isValidId = isValidId.slice(0, 30)
                    setFormData((prev) => ({ ...prev, [name]: isValidId }));
                    openAlert("아이디는 4자 이상 30자 이하로 입력해주세요.");
                    return;
                }
            } else if (name === "birth") {

                let birthVal = value;
                let month = birthVal.slice(5, 2);
                let day = birthVal.slice()
                if (birthVal.length < 10) {
                    openAlert('생년월일 형식을 맞춰주세요 \n ex) 1990-01-01')
                    return false;
                } else {
                    setFormData((prev) => ({ ...prev, [name]: value }));
                }

            } else if (name === "passwd" || name === "passwdConf") {

                let passwdChkVal = value;

                // 정규식 설명:
                // ^                        → 문자열 시작
                // (?=.*[A-Za-z])          → 적어도 하나의 영문자
                // (?=.*\d)                → 적어도 하나의 숫자
                // (?=.*[#!@$])            → 적어도 하나의 특수문자 (#, !, @, $)
                // [A-Za-z\d#!@$]{8,20}    → 전체는 위 문자들로만 구성되며 길이는 8~20자
                // $                        → 문자열 끝
                const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[#!@$])[A-Za-z\d#!@$]{8,20}$/;

                let isValidId = regex.test(passwdChkVal);

                if (!isValidId) {

                    openAlert("비밀번호 양식을 확인해주세요 \n 비밀번호는 영문, 숫자, 특수문자(#,!,@,$)를 포함한 8~20자여야 합니다.");

                    if (passwdChkVal.length > 20) {
                        passwdChkVal = passwdChkVal.slice(0, 20);
                    }
                    setFormData((prev) => ({ ...prev, [name]: passwdChkVal }));
                    return false;
                }
            } else {
                setFormData((prev) => ({ ...prev, [name]: value }));
            }
        }
    };

    const inputChgVal = (e) => {

        const { name, value } = e.target;

        //전화번호
        if (name === "phoneNum") {

            let rvHyphon = value.replace(/\D/g, ""); // 숫자만 남김

            // 입력 길이 제한
            let maxLength = 11;

            if (rvHyphon.startsWith("02")) {
                maxLength = 10;
            }

            if (rvHyphon.length > maxLength) {
                rvHyphon = rvHyphon.slice(0, -1);
            }
            setFormData({ ...formData, [name]: formatPhoneNumber(rvHyphon) });

        } else if (name === "birth") {

            setFormData({ ...formData, [name]: formatBirth(value) })
        } else {
            setFormData({ ...formData, [name]: value });
        }
    };

    //길이제한 공통 함수
    const chkInputLength = (e) => {
        const { name, value } = e.target;
        debugger;
        let maxLength = 0;
        let label = "";
        let chkVal = value.trim().replace(/\s/g, ""); // 모든 문자 공백제거


        if (name == "userName") {
            maxLength = 20;
            label = "이름";

        } else if (name == "userId") {
            maxLength = 30;
            label = "아이디";

        } else if (name == "passwd" || name == "passwdConf") {
            maxLength = 30;
            label = "비밀번호";

        } else if (name == "email") {
            maxLength = 30;
            label = "email";
        }

        if (maxLength > 0 && chkVal.length > maxLength) {
            openAlert(label + '은/는 최대' + maxLength + '자까지 입력 가능합니다.');
            setFormData({ ...formData, [name]: chkVal.slice(0, maxLength) });
            return false;
        } else {
            setFormData({ ...formData, [name]: chkVal });
        }
    }

    // 전화번호 형식 변환 함수
    const formatPhoneNumber = (value) => {

        const cleaned = value;

        if (cleaned.startsWith("02")) {
            // 서울 지역번호 처리
            if (cleaned.length === 9) {
                return `${cleaned.slice(0, 2)}-${cleaned.slice(2, 5)}-${cleaned.slice(5)}`; // 02-XXX-XXXX
            } else if (cleaned.length === 10) {
                return `${cleaned.slice(0, 2)}-${cleaned.slice(2, 6)}-${cleaned.slice(6)}`; // 02-XXXX-XXXX
            } else if (cleaned.length > 11) {
                return cleaned.slice(0);
            }
        } else {
            // 기타 번호 처리
            if (cleaned.length === 10) {
                return `${cleaned.slice(0, 3)}-${cleaned.slice(3, 6)}-${cleaned.slice(6)}`; // 000-000-0000
            } else if (cleaned.length === 11) {
                return `${cleaned.slice(0, 3)}-${cleaned.slice(3, 7)}-${cleaned.slice(7)}`; // 000-0000-0000
            } else if (cleaned.length > 11) {

                return cleaned.slice(0);
            }
        }
        return cleaned;
    };

    //생년월일 형식 변환
    const formatBirth = (inputVal) => {

        let setVal = inputVal;
        let maxLength = 10;

        if (inputVal.length > maxLength) {

            setVal = setVal.slice(0, -1);
        }

        if (inputVal.length == 4 || inputVal.length == 7) {

            setVal = setVal + '-';
        }

        return setVal;
    }

    const handleClickShowPassword = () => setShowPassword((show) => !show);
    const handleClickShowPasswordConf = () => setShowPasswordConf((show) => !show);

    const handleMouseDownPassword = (event) => {
        event.preventDefault();
    };
    const handleMouseUpPassword = (event) => {
        event.preventDefault();
    };

    //vliadation
    const chkReqValidForm = (e, formDataState, formRef) => {
        let inputArr = formRef.current.querySelectorAll("input");
        let labelArr = formRef.current.querySelectorAll("label");
        let formDataKeys = Object.keys(formDataState);

        //formDataKey = input name
        for (let j = 0; j < formDataKeys.length; j++) {
            let formDataKey = formDataKeys[j];
            let inputTag = formRef.current.querySelector('input[name="' + formDataKey + '"]');

            let labelNm = "";
            let chkVal = "";

            //inputBox
            if (inputTag != null) {
                //필수값여부가 Y이면
                if (inputTag.required) {

                    chkVal = formDataState[formDataKey];

                    let inputTagId = inputTag.id;

                    if (inputTag.className.toLowerCase().indexOf("muiselect") != -1) {
                        //SELECT 박스 label은 구조상 input의 부모와  형제노드로 존재
                        let parent = inputTag.parentElement.parentElement;

                        labelNm = parent.querySelector('label').innerText.replaceAll("*", "");
                        chkVal = formDataState[formDataKey];

                        if (!chkVal) {
                            openAlert(labelNm + "은/는 필수 입력값입니다.")
                            return false;
                        }
                    } else {

                        for (let k = 0; k < labelArr.length; k++) {

                            let labelTag = labelArr[k];
                            let reqLabelTag = formRef.current.querySelector('label[for="' + inputTagId + '"]');

                            if (inputTagId === labelTag.getAttribute("for")) {
                                labelNm = labelTag.innerText.replaceAll("*", "");

                                if (!chkVal) {
                                    openAlert(labelNm + "은/는 필수 입력값입니다.")
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    return (
        <>
            <Box sx={{ display: 'flex', justifyContent: 'center', marginTop: '50px' }} ref={userForm}>
                <Stack direction={'column'} sx={{ width: '50%' }}>
                    <FormControl required>

                        <InputLabel htmlFor="userName"> 이름 </InputLabel>
                        <Input
                            id="userName"
                            name="userName"
                            value={formData.userName}
                            required
                            onChange={(e) => inputChgVal(e)}
                            onBlur={(e) => inputBlur(e)}
                            onCompositionStart={() => handleCompositionStart(true)}
                            onCompositionEnd={(e) => {
                                handleCompositionEnd(false);
                                //inputChgVal(e); // 조합이 끝난 후 마지막 값 반영
                            }}
                        />
                    </FormControl>

                    <FormControl sx={{ marginTop: '10px' }} required>
                        <InputLabel htmlFor="birth"> 생년월일 </InputLabel>
                        <Input
                            id="birth"
                            name="birth"
                            required
                            value={formData.birth}
                            onChange={(e) => inputChgVal(e)}
                            onBlur={(e) => inputBlur(e)}
                        />

                    </FormControl>
                    <FormControl sx={{ marginTop: '10px' }} required>

                        <Stack direction={"row"} alignItems="center">

                            <InputLabel htmlFor="userId"> 아이디 </InputLabel>
                            <Input
                                id="userId"
                                name="userId"
                                value={formData.userId}
                                required
                                onChange={inputChgVal}
                                onBlur={(e) => inputBlur(e)}
                            />

                            <Button variant='contained' onClick={() => getIdDuplicateChk()}> 중복췍 </Button>

                        </Stack>
                    </FormControl>
                    <Stack direction={'row'} justifyContent={'space-between'} sx={{ marginTop: '10px' }}>
                        <FormControl required>
                            <InputLabel htmlFor="standard-adornment-password">패스워드</InputLabel>
                            <Input
                                id="standard-adornment-password"
                                name="passwd"
                                value={formData.passwd}
                                type={showPassword ? 'text' : 'password'}
                                required
                                endAdornment={
                                    <InputAdornment position="end">
                                        <IconButton
                                            aria-label={
                                                showPassword ? 'hide the password' : 'display the password'
                                            }
                                            onClick={handleClickShowPassword}
                                            onMouseDown={handleMouseDownPassword}
                                            onMouseUp={handleMouseUpPassword}

                                        >
                                            {showPassword ? <VisibilityOff /> : <Visibility />}
                                        </IconButton>
                                    </InputAdornment>
                                }
                                onChange={inputChgVal}
                                onBlur={(e) => inputBlur(e)}
                            />
                        </FormControl>
                        <FormControl required>
                            <InputLabel htmlFor="standard-adornment-passwordConf">패스워드 확인</InputLabel>
                            <Input
                                id="standard-adornment-passwordConf"
                                name="passwdConf"
                                value={formData.passwdConf}
                                type={showPasswordConf ? 'text' : 'password'}
                                required
                                endAdornment={
                                    <InputAdornment position="end">
                                        <IconButton
                                            aria-label={
                                                showPasswordConf ? 'hide the password' : 'display the password'
                                            }
                                            onClick={handleClickShowPasswordConf}
                                            onMouseDown={handleMouseDownPassword}
                                            onMouseUp={handleMouseUpPassword}
                                            onBlur={(e) => inputBlur(e)}

                                        >
                                            {showPasswordConf ? <VisibilityOff /> : <Visibility />}
                                        </IconButton>
                                    </InputAdornment>
                                }
                                onChange={inputChgVal}
                            />
                        </FormControl>

                    </Stack>

                    <FormControl sx={{ marginTop: '10px' }} required>
                        <InputLabel htmlFor="phoneNum" > 휴대폰번호 </InputLabel>
                        <Input
                            id="phoneNum"
                            name="phoneNum"
                            value={formData.phoneNum}
                            required
                            onChange={inputChgVal}
                        />
                    </FormControl>
                    <FormControl sx={{ marginTop: '10px' }} required>
                        <InputLabel htmlFor="email"> 이메일 </InputLabel>
                        <Input
                            id="email"
                            name="email"
                            value={formData.email}
                            required
                            onChange={inputChgVal}
                        />
                    </FormControl>

                    <FormControl sx={{ marginTop: '10px' }}>
                        <Stack direction={"row"} alignItems="center">
                            <InputLabel htmlFor="address"> 주소 </InputLabel>
                            <Input
                                id="address"
                                name="address"
                                value={formData.address}
                                onChange={inputChgVal}
                                sx={{ width: '95%' }}
                                readOnly
                            />

                            {/*
                            (부모 -> 자식 데이터전달 ) : setAddress라는 prop을 전달 
                        
                            */}
                            <DaumAddressApi setAddress={(addr) => setFormData((prev) => ({ ...prev, address: addr }))} />

                        </Stack>
                    </FormControl>

                    <FormControl sx={{ marginTop: '10px' }}>
                        <InputLabel htmlFor="dtlAddress"> 상세 주소 </InputLabel>
                        <Input
                            id="dtlAddress"
                            name="dtlAddress"
                            value={formData.dtlAddress}
                            onChange={inputChgVal}
                        />
                    </FormControl>
                    <Box sx={{ marginTop: '20px' }}>
                        <Button variant='contained' onClick={(e) => openConfirm(e, postUserData)}> submit </Button>
                    </Box>
                </Stack >


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

            </Box >
        </>
    );
}

export default RegisterUserPage;

