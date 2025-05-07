function checkCookie(name) {
    // document.cookie는 모든 쿠키를 "key=value" 형태로 문자열로 반환합니다.
    // 쿠키 존재 여부를 확인하기 위해 해당 이름이 쿠키 문자열에 포함되어 있는지 확인
    return document.cookie.split(';').some((item) => item.trim().startsWith(name + '='));
}


function getTokenFromCookie(name) {
    console.log("aksjdlas::", document.cookie);

    debugger;
    const matches = document.cookie.match(new RegExp(
        "(?:^|; )" + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + "=([^;]*)"
    ));
    return matches ? decodeURIComponent(matches[1]) : undefined;


}

function parseJwt(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');

        // padding(=) 추가
        const padded = base64.padEnd(base64.length + (4 - base64.length % 4) % 4, '=');

        const jsonPayload = atob(padded);
        return JSON.parse(jsonPayload);
    } catch (e) {
        console.log('토큰 파싱 에러', e);
        return null;
    }
}

function getUserNameFromToken(token) {
    const resPayload = parseJwt(token);

    return resPayload ? resPayload.userName : null;

}

function getExpiredToken(token) {

    const resPayload = parseJwt(token);

    if (!resPayload || !resPayload.exp) return true;

    const currentTime = Math.floor(Date.now() / 1000);

    return resPayload.exp < currentTime;
}


export default {
    checkCookie,
    getTokenFromCookie,
    parseJwt,
    getUserNameFromToken,
    getExpiredToken
}