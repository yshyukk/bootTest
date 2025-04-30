package example.demo.main.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.demo.config.JwtProvider;
import example.demo.main.service.LoginDto;
import example.demo.main.service.LoginService;
import example.demo.token.service.TokenMngService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class LoginController {
	
	@Autowired
	LoginService loginService;
	@Autowired
	JwtProvider jwtProvider;
	@Autowired
	TokenMngService tokenMngService;
	
	@PostMapping("/doLogin")
	public ResponseEntity<LoginDto> doLogin(@RequestBody Map<String, Object> loginParam, HttpServletResponse response, HttpServletRequest request) throws Exception{
		
		Map<String, Object> res = new HashMap<String, Object>();
		
		LoginDto LoginConfDto = loginService.doLogin(loginParam, response, request);
		
		
		return ResponseEntity.ok().body(LoginConfDto);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		// 1. JWT 쿠키 삭제 (덮어쓰기)
	    Cookie cookie = new Cookie("accessToken", null);
	    cookie.setHttpOnly(true); //js에서 쿠키 접근 못하게
	    cookie.setSecure(true); //https환경에서만 쿠키 전송되도록
	    cookie.setPath("/"); // 전체 사이트에서 쿠키 삭제
	    cookie.setMaxAge(0); // 즉시 만료
	    response.addCookie(cookie);
	    
	    // 2. RefreshToken 쿠키 삭제
	    Cookie refreshCookie = new Cookie("refreshToken", null); // 쿠키 이름은 저장할 때 썼던 이름과 정확히 일치해야 함
	    refreshCookie.setHttpOnly(true);
	    refreshCookie.setSecure(true);
	    refreshCookie.setPath("/");
	    refreshCookie.setMaxAge(0);
	    response.addCookie(refreshCookie);
	    
	    
	    loginService.logout(request, response);
	    
	    // 3. SecurityContext 초기화 (Spring Security 쓰면 추가적으로)
	    //서버에서 인증정보(SecurityContext) 남아있는 걸 깔끔하게 지워주는 역할.
	    SecurityContextHolder.clearContext();

	    return ResponseEntity.ok("로그아웃 성공");
	}
	
}
