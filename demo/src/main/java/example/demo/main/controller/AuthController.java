package example.demo.main.controller;

import example.demo.config.JwtProvider;
import example.demo.main.service.LoginDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtProvider jwtProvider;

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(HttpServletRequest request, HttpServletResponse response) {
        
        String refreshToken = getCookieValue(request, "RefreshToken");

        if (refreshToken != null && jwtProvider.validateToken(refreshToken)) {
            String userMngSn = jwtProvider.getUserIdFromToken(refreshToken);

            // AccessToken 재발급
            LoginDto dummyLoginDto = new LoginDto();
            dummyLoginDto.setUserId(userMngSn);

            String newAccessToken = jwtProvider.generateToken(dummyLoginDto);

            Cookie accessCookie = new Cookie("accessToken", newAccessToken);
            accessCookie.setHttpOnly(true);
            accessCookie.setSecure(true);
            accessCookie.setPath("/");
            accessCookie.setMaxAge(60 * 60 * 1); // 1시간
            response.addCookie(accessCookie);
            
            // ② 헤더에 토큰 추가 (프론트에서 받을 수 있게)
            //response.setHeader("Authorization", "Bearer " + newAccessToken);
            System.out.println("==================================================== ");
            System.out.println("Authorization header set: Bearer " + newAccessToken);
            System.out.println("==================================================== ");
            return ResponseEntity.ok()
            		.header("Authorization", "Bearer " + newAccessToken)
            		.build();
        } else {
            return ResponseEntity.status(401).build(); // RefreshToken 만료 or 이상 -> 401
        }
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
