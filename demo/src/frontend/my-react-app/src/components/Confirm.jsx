import * as React from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogContentText,
    DialogActions,
    Button
} from '@mui/material';

const ConfirmDialog = ({ open, onClose, onConfirm, message }) => {
    return (
        <Dialog open={open} onClose={onClose}>
            <DialogTitle>확인</DialogTitle>
            <DialogContent>
                <DialogContentText>
                    {message || '정말로 진행하시겠습니까?'}
                </DialogContentText>
            </DialogContent>
            <DialogActions>
                <Button onClick={onClose}>취소</Button>
                <Button onClick={onConfirm} autoFocus>
                    확인
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default ConfirmDialog;
