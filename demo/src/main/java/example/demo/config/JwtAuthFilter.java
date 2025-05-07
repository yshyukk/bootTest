package example.demo.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import example.demo.main.service.LoginDto;
import example.demo.token.service.TokenMngService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
	
	private final JwtProvider jwtProvider;
	private final TokenMngService tokenMngService;
	
	/*
	 * 토큰 검증
	 * 클라이언트에서 요청올때마다 filterChain반복 -> 검증
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
			String token = resolveToken(request); // HTTP reqeust 헤더에서 "Authorization"을 찾아 Bearer 형식의 토큰 추출
			
			/*
			 * jwtProvider.validateToken(token)  
			 * 추출한 토큰 검증
			 * 유효하면 -> 토큰에 포함된 사용자 정보를 사용해 인증
			 */
			 if (token != null && jwtProvider.validateToken(token)) {
		            String userMngsn = jwtProvider.getUserIdFromToken(token);
		            
		            // UsernamePasswordAuthenticationToken(username, null, List.of());
		            // 두번째값은 권한! 
		            
		            UsernamePasswordAuthenticationToken auth =
		                new UsernamePasswordAuthenticationToken(userMngsn, null, List.of());
		            
		            /*
		             * 인증이 성공하면 SecurityText에 인증 정보를 저장하여
		             * 현재 요청에 대한 인증을 설정함.
		             * 
		             *  이후 요청에서 인증된 사용자의 정보를 참조할 수 있게 해줌
		             */
		            SecurityContextHolder.getContext().setAuthentication(auth);

			 }else {
	        	//AccessToken이 유효하지 않거나 만료되었을 경우
				 String refreshToken = resolveRefreshToken(request);
				 
				 
			        if (refreshToken != null && jwtProvider.validateToken(refreshToken)) {
			            LoginDto loginDto = new LoginDto();
			        	
			        	String userMngsn = jwtProvider.getUserIdFromToken(refreshToken);
			            	
			        	Map<String, Object> param = new HashMap<String, Object>();
			        	
			        	param.put("userMngSn", userMngsn);
			        	param.put("refreshToken", refreshToken);			
			        	
			            // RefreshToken을 DB에서 확인 (Revoke 여부, 유효성 체크)
			        	
			        	try {
							loginDto = tokenMngService.findByUserInfoIsRevokedFalseToken(param);
							
							if(!ObjectUtils.isEmpty(loginDto.getRefreshToken())) {
								
				                // DB에서 유효한 RefreshToken을 찾았다면, 새로운 AccessToken을 발급
				                String newAccessToken = jwtProvider.generateRefreshToken(loginDto);  // 새로운 AccessToken 생성

				                // 새로운 AccessToken을 클라이언트에게 전달
				                response.setHeader("Authorization", "Bearer " + newAccessToken);
				                
				                Cookie newAccessCookie = new Cookie("accessToken", newAccessToken);
				                newAccessCookie.setHttpOnly(true);
				                newAccessCookie.setSecure(false);
				                newAccessCookie.setPath("/");
				                newAccessCookie.setMaxAge(60 * 30); // 30분
				                response.addCookie(newAccessCookie);

				                
				                // ★ 인증 처리까지 해줘야 함
				                UsernamePasswordAuthenticationToken auth =
				                    new UsernamePasswordAuthenticationToken(userMngsn, null, List.of());
				                SecurityContextHolder.getContext().setAuthentication(auth);
				                
				                filterChain.doFilter(request, response);
				                return;
				                
							}else {
								
								response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid refresh token");
								return;
							}
						} catch (Exception e) {

							e.printStackTrace();
							response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token check error");
					        return;
						}
			            
			        } else {
			        	 // 최초 로그인 시 refreshToken이 없을 경우
			            // 최초 로그인 시에는 refreshToken을 발급할 수 없기 때문에, 일반적으로 인증을 통과시켜줍니다.
			            //filterChain.doFilter(request, response);
			        	
			        	if(refreshToken == null) {
			        		 filterChain.doFilter(request, response);  // 필터 체인 계속 진행
			        	        return;
			        	}
			        	
			        	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No refresh token or invalid refresh token");
			            return;
			        }
	         }
			 	// 필터 체인 계속 진행
		        filterChain.doFilter(request, response);
	}
	
	/*
	 *  HTTP Request 헤더에서 Authrization 값을 찾아 Bearer로 시작하는 토큰을 추출
	 *  이후 Bearer 앞부분을 제외한 실제 JWT 토큰을 반환함.
	 *  Bearer가 없거나 형식이 맞지 않으면 NULL을 반환
	 * */
	
	private String resolveToken(HttpServletRequest request) {
		
		String bearer = request.getHeader("Authorization");
			
		if(bearer != null && bearer.startsWith("Bearer ")) {
			return bearer.substring(7);
		}
		
	    // 👇 쿠키에서 accessToken을 추출
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if ("accessToken".equals(cookie.getName())) {
	                return cookie.getValue();
	            }
	        }
	    }
		return null;
	}
	
	/*
	 *  HTTP Request 헤더에서 Authrization 값을 찾아 Bearer로 시작하는 토큰을 추출
	 *  이후 Bearer 앞부분을 제외한 실제 JWT 토큰을 반환함.
	 *  Bearer가 없거나 형식이 맞지 않으면 NULL을 반환
	 * */
	
	private String resolveRefreshToken(HttpServletRequest request) {
		
		Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if ("refreshToken".equals(cookie.getName())) {  // "RefreshToken" 쿠키를 찾음
	                return cookie.getValue();  // 쿠키 값 (RefreshToken)을 반환
	            }
	        }
	    }
	    
	    return null;  // 만약 Authorization 헤더와 쿠키 모두 없으면 null 반환
	}
	
	// 로그인 등의 인증 API는 이 필터를 통과하지 않도록 예외처리
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/api/doLogin"); // 필요시 다른 경로도 추가
    }
	
}
