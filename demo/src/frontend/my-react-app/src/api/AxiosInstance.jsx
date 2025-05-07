import axios from "axios";

const AxiosInstance = axios.create({
    baseURL: 'http://localhost:8080',
    timeout: 5000,
    headers: {
        'Content-Type': 'application/json',

    },
    withCredentials: true // 쿠키 포함해서 요청 보내기
});

// refresh 토큰으로 access 토큰 재발급
async function refreshAccessToken() {

    try {
        const res = await AxiosInstance.post("/auth/refresh", null, { withCredentials: true });

        const newAccessToken = res.headers['authorization']?.replace("Bearer ", "");

        if (newAccessToken) {

            AxiosInstance.defaults.headers.common['Authorization'] = `Bearer ${newAccessToken}`;
            localStorage.setItem("accessToken", newAccessToken);
            return true;
        }
        return false;
    } catch (error) {
        return false;
    }
}



/*
async function refreshAccessToken() {
    try {
        await AxiosInstance.post("/auth/refresh", null, { withCredentials: true });
        return true;
    } catch (error) {
        return false;
        //access토큰이 만료 또는 오류 => 재로그인 로직 필요
    }
}
*/
// 응답 인터셉터 추가
/* 만약, 장시간 대기하고있다가 AccessToken이 만료되면
* Axios서버가 API 요청을 보내고 서버가 401 Unauthorized 에러를 응답
* -> Axios 응답 인터셉터가 잡는다
* -> refresh토큰으로 AccessToken 재발급을 시도한다
* -> 실패하면 -> 인터셉터 안에서 Promise.reject(error)로 컴포넌트 쪽으로 넘긴다.
* => 그럼 후처리는?
*/
AxiosInstance.interceptors.response.use(

    (response) => {
      
        //서버 응답에서 Authorization 헤더로 새 accessToken이 내려온 경우 저장
        const newAccessToken = response.headers['authorization'];

        //console.log("응답 헤더:", response.headers); // 👈 로그 찍기

        if (newAccessToken) {
            const token = newAccessToken.replace("Bearer ", "");
            localStorage.setItem("accessToken", token);
            AxiosInstance.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        }
        return response;
    },
    async (error) => {
        const originalRequest = error.config;

        //access토큰 만료 감지 (401 오류 + retry 안 했을 때)
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true; // 무한루프 방지 (한번만 재시도)

            //localStorage.clear();

            //Access토큰 재발급
            const newToken = await refreshAccessToken();

            if (newToken) {
                // ③ 재발급 받은 토큰을 Authorization 헤더에 추가하고 요청 재시도
                const token = localStorage.getItem("accessToken");
                if (token) {
                    originalRequest.headers["Authorization"] = `Bearer ${token}`;
                }

                return AxiosInstance(originalRequest); // 실패한 요청 재시도

            } else {

                //********************************로그인 만료 알람 띄우기 추가
                window.location.href = "/LoginForm"; // 리프레시도 실패하면 로그인페이지
                return Promise.reject(error);
                /*
                    Promis 종류
                     - Pending  : 대기중 
                     - Fulfilled : 성공 완료 (resolved 호출됨)
                     - Rejected  : 실패 완료 (reject 호출됨)
                     
                     try catch문으로 잡을 수 있음
                     Promise.reject(new Error('에러 발생'))
                    .catch(error => console.log(error.message));
    
    
                    interceptor안에서 return Promise.reject(error)를 사용하는 이유
                     - 에러를 Axios를 호출한 컴포넌트 등에서 처리한다는 뜻
                     - 사용하지않으면
                       - 인터셉터에서 에러를 그냥 먹어버려서 밖에서는 성공한 것 처럼 보임
                       - 프론트엔드에서 catch문 작동 안함
                */
            }

        }

        return Promise.reject(error);
    }
);

//로그아웃
const logout = async () => {
    try {
        await AxiosInstance.post("/api/logout"); // 로그아웃 API 호출

        // 1. sessionStorage 비우기
        sessionStorage.clear();

        // 2. 로그인 페이지로 이동
        window.location.href = "/login";
    } catch (error) {
        console.error("로그아웃 실패", error);
        alert("로그아웃 중 오류가 발생했습니다.");
    }
};
export default AxiosInstance;