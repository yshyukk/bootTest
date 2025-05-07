import * as React from 'react';
import { useState, useEffect } from 'react';
import Box from '@mui/material/Box';
import Avatar from '@mui/material/Avatar';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import Divider from '@mui/material/Divider';
import Typography from '@mui/material/Typography';
import PersonAdd from '@mui/icons-material/PersonAdd';
import Settings from '@mui/icons-material/Settings';
import Logout from '@mui/icons-material/Logout';

import { Link } from "react-router-dom";
import AxiosInstance from '../../api/AxiosInstance';
import logout from '../../api/AxiosInstance';
import Alert from '../../components/Alert';
import Confirm from '../../components/Confirm';


import { useAuth } from '../../context/LoginContext'; //로그인 정보 관리 (전역)

const Menubar = () => {

    const [anchorEl, setAnchorEl] = React.useState(null);
    const open = Boolean(anchorEl);

    const { loginInfo, logout } = useAuth();

    //Alert
    const [alertOpen, setAlertOpen] = useState(false);
    const [alertMsg, setAlertMsg] = useState('');

    const [openConf, setOpenConf] = React.useState(false);

    useEffect(() => {

    }, [])


    const openAlert = (msg) => {
        setAlertMsg(msg);
        setAlertOpen(true);
    };
    const openConfirm = (e, callback) => {

        if (callback) {

            setConfAction(() => () => callback(e));
        }
        setOpenConf(true);

    }

    const handelConf = () => {
        confAction(); // state에 저장된 함수 실행
        setOpenConf(false);
    }



    const handleClose = () => {
        setAnchorEl(null);
    };

    const doLogout = async (e) => {
        try {
            await AxiosInstance.post("/api/logout"); // 로그아웃 API 호출
            logout(); //context에 logout호출

            window.location.href = "/loginForm";
        } catch (error) {
            console.error("로그아웃 실패", error);
            openAlert("로그아웃 중 오류가 발생했습니다.");
        }
    };

    return (
        <React.Fragment>

            <Box sx={{ display: 'flex', alignItems: 'center', textAlign: 'center' }}>

                <Link to="/" style={{ textDecoration: 'none', color: 'inherit' }}>
                    <Typography sx={{ minWidth: 100 }}>Home</Typography>
                </Link>

                <Link to="/RegisterUser" style={{ textDecoration: 'none', color: 'inherit' }}>
                    <Typography sx={{ minWidth: 100 }}>Register</Typography>
                </Link>

                <Link to="/BoardListPage" style={{ textDecoration: 'none', color: 'inherit' }}>
                    <Typography sx={{ minWidth: 100 }}>Board</Typography>
                </Link>

                {loginInfo ? (
                    <Box onClick={doLogout} sx={{ cursor: "pointer" }}>
                        <Typography sx={{ minWidth: 100 }}>Logout</Typography>
                    </Box>
                ) : (
                    <Link to="/LoginForm" style={{ textDecoration: 'none', color: 'inherit' }}>
                        <Typography sx={{ minWidth: 100 }}>Login</Typography>
                    </Link>
                )}



                {/**
                 * 로그인 후처리 작업 필요
                 * 
                 * 
                */}

                {/**
                <Typography sx={{ minWidth: 100 }}>Profile</Typography>

                <Tooltip title="Account settings">
                    <IconButton
                        onClick={handleClick}
                        size="small"
                        sx={{ ml: 2 }}
                        aria-controls={open ? 'account-menu' : undefined}
                        aria-haspopup="true"
                        aria-expanded={open ? 'true' : undefined}
                    >
                        <Avatar sx={{ width: 32, height: 32 }}>M</Avatar>
                    </IconButton>
                </Tooltip>
                 */}
            </Box>
            <Menu
                anchorEl={anchorEl}
                id="account-menu"
                open={open}
                onClose={handleClose}
                onClick={handleClose}
                slotProps={{
                    paper: {
                        elevation: 0,
                        sx: {
                            overflow: 'visible',
                            filter: 'drop-shadow(0px 2px 8px rgba(0,0,0,0.32))',
                            mt: 1.5,
                            '& .MuiAvatar-root': {
                                width: 32,
                                height: 32,
                                ml: -0.5,
                                mr: 1,
                            },
                            '&::before': {
                                content: '""',
                                display: 'block',
                                position: 'absolute',
                                top: 0,
                                right: 14,
                                width: 10,
                                height: 10,
                                bgcolor: 'background.paper',
                                transform: 'translateY(-50%) rotate(45deg)',
                                zIndex: 0,
                            },
                        },
                    },
                }}
                transformOrigin={{ horizontal: 'right', vertical: 'top' }}
                anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
            >
                <MenuItem onClick={handleClose}>
                    <Avatar /> Profile
                </MenuItem>
                <MenuItem onClick={handleClose}>
                    <Avatar /> My account
                </MenuItem>
                <Divider />
                <MenuItem onClick={handleClose}>
                    <ListItemIcon>
                        <PersonAdd fontSize="small" />
                    </ListItemIcon>
                    Add another account
                </MenuItem>
                <MenuItem onClick={handleClose}>
                    <ListItemIcon>
                        <Settings fontSize="small" />
                    </ListItemIcon>
                    Settings
                </MenuItem>
                <MenuItem onClick={handleClose}>
                    <ListItemIcon>
                        <Logout fontSize="small" />
                    </ListItemIcon>
                    Logout
                </MenuItem>
            </Menu>

            <Alert
                open={alertOpen}
                message={alertMsg}
                onClose={() => setAlertOpen(false)}
            />
            <Confirm
                open={openConf}
                onClose={() => setOpenConf(false)}
                onConfirm={handelConf}
            />

        </React.Fragment>
    );
}

export default Menubar;