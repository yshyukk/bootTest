import * as React from 'react';
import dayjs, { Dayjs } from 'dayjs';
import { DemoContainer } from '@mui/x-date-pickers/internals/demo';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';

const DatePicker = () => {
    const [value, setValue] = React.useState < Dayjs | null > (dayjs('2022-04-17'));

    return (

        <DemoContainer components={['DatePicker', 'DatePicker']}>
            <DatePicker
                label="Controlled picker"
                value={value}
                onChange={(newValue) => setValue(newValue)}
            />
        </DemoContainer>

    );
}

export default DatePicker;


/* 호출해서 사용법

         <LocalizationProvider dateAdapter={AdapterDayjs}>

            <DatePicker

                value={formData.birth ? dayjs(formData.birth) : null}
                onChange={(newValue => setFormData(prev => ({
                    ...prev,
                    birth: newValue ? newValue.format('YYYY-MM-DD') : ''
                })))}
            />
        </LocalizationProvider>

    - <LocalizationProvider dateAdapter={AdapterDayjs}> : DatePicker컴포넌트 감싸야함
    - value={formData.birth ? dayjs(formData.birth) : null} 넘어온 값을 dayjs format으로 변환해야함.

*/