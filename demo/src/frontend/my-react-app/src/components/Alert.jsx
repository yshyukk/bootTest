// components/AlertDialog.js
import React from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogContentText,
    DialogActions,
    Button,
} from '@mui/material';

const Alert = ({ open, message, onClose }) => {
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>알림</DialogTitle>
            <DialogContent>
                <DialogContentText>{message}</DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose} autoFocus>확인</Button>
            </DialogActions>
        </Dialog>
    );
};

export default Alert;
