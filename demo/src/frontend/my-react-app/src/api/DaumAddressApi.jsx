import React from "react";
import { useDaumPostcodePopup } from 'react-daum-postcode'; // Daum 주소 검색 관련 hook
import SearchSharpIcon from '@mui/icons-material/SearchSharp';

const DaumAddressApi = ({ setAddress }) => {
    const postcodeScriptUrl = 'https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
    const open = useDaumPostcodePopup(postcodeScriptUrl);

    const handleComplete = (data) => {
        debugger;
        let fullAddress = data.address;
        let extraAddress = '';
        let localAddress = data.sido + ' ' + data.sigungu;

        if (data.addressType === 'R') {
            if (data.bname !== '') {
                extraAddress += data.bname;
            }
            if (data.buildingName !== '') {
                extraAddress += (extraAddress !== '' ? `, ${data.buildingName}` : data.buildingName);
            }
            //fullAddress = fullAddress.replace(localAddress, '');
            fullAddress += (extraAddress !== '' ? ` (${extraAddress})` : '');
        }

        setAddress(fullAddress); // setAddress를 호출하여 부모 컴포넌트의 상태를 업데이트
    };

    const handleClick = () => {
        open({ onComplete: handleComplete });
    };

    return (

        <>
            <SearchSharpIcon onClick={handleClick} />
        </>

    );
}

export default DaumAddressApi;