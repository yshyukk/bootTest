package example.demo.main.service.Impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import example.demo.config.JwtProvider;
import example.demo.main.dao.LoginMapper;
import example.demo.main.service.LoginDto;
import example.demo.main.service.LoginService;
import example.demo.token.service.TokenMngService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{
	
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final LoginMapper loginMapper;
	private final MessageSource messageSoruce;
	private final TokenMngService tokenMngService;
	
	
	@Override
	@Transactional
	public LoginDto doLogin(Map<String, Object> loginParam, HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		LoginDto loginDto = new LoginDto();
		

		if(!ObjectUtils.isEmpty(loginParam)){
			
			String encodePasswd = "";
			String inputPasswd = loginParam.get("passwd").toString();
			Boolean matchesYn = false;

			//아이디 체크
			loginDto = loginMapper.getLoginUserChkId(loginParam);

			if(loginDto != null) {
				
				//비밀번호 가져오기
				encodePasswd = loginMapper.getPassWd(loginParam);
				matchesYn = passwordEncoder.matches(inputPasswd, encodePasswd);
				
			}
			
			if(loginDto == null || !matchesYn) {
				
				//Security exception 예외처리 (경고창 필요할듯) -- 이걸 타도 아래 로직 수행함..
				throw new BadCredentialsException("msg.error.badcredentials");
			}

			//Access Token 생성
			String accessToken = jwtProvider.generateToken(loginDto);
			
			//loginDto.setToken(jwtProvider.generateToken(loginDto));
			
			//JWT 토큰을 쿠키에 저장
			Cookie accessCookie = new Cookie("accessToken", accessToken);
			accessCookie.setHttpOnly(true); // js에서 쿠키에 접근할수 없도록
			accessCookie.setSecure(false);   // HTTPS에서만 쿠키가 전송되도록 설정
			accessCookie.setPath("/");	  //쿠키의 유효범위
			accessCookie.setMaxAge(60 * 60 * 1); //유효시간 : 24시간
			
			//refresh Token 생성
			String refreshToken = jwtProvider.generateRefreshToken(loginDto);
			
			Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
			refreshCookie.setHttpOnly(true); // js에서 쿠키에 접근할수 없도록
			refreshCookie.setSecure(false);   // HTTPS에서만 쿠키가 전송되도록 설정 (개발용 : false)
			refreshCookie.setPath("/");	  //쿠키의 유효범위
			refreshCookie.setMaxAge(60 * 60 * 1); //유효시간 : 24시간
			
			Map<String, Object> tokenInfo = new HashMap<String, Object>();
			
			//refreshToken 발급시간 
			long longTypeIsuuedTime = jwtProvider.getIssuedAt(refreshToken);

			Timestamp issuedTime =  new Timestamp(longTypeIsuuedTime);
			loginDto.setIssuedDt(issuedTime);
			
			//만료시간 구하기
			long longTypeExpireTime = jwtProvider.getIssuedAt(refreshToken);
			
			Timestamp expiresTime = new Timestamp(longTypeExpireTime);
			
			loginDto.setExpiresDt(expiresTime);
			
			//클라이언트 ip
			String ipAddress = request.getHeader("X-Forwarded-For");
			if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
			    ipAddress = request.getRemoteAddr();
			}
			
			//접속한 브라우저 정보
			String usedBrowser = request.getHeader("User-Agent");
			
			
			tokenInfo.put("refreshToken", refreshToken);
			tokenInfo.put("userMngSn", loginDto.getUserMngSn());
			tokenInfo.put("issuedTime", loginDto.getIssuedDt());	
			tokenInfo.put("expiresDt", loginDto.getExpiresDt());	
			tokenInfo.put("ipAddress", ipAddress);	
			tokenInfo.put("usedBrowser", usedBrowser);	
			
			tokenMngService.refreshTokenCreateInfo(tokenInfo);
			
			response.addCookie(accessCookie);
			response.addCookie(refreshCookie);
			
			/*SpringSecurity의 authentication 설정*/
			UsernamePasswordAuthenticationToken auth =
				    new UsernamePasswordAuthenticationToken(loginDto.getUserMngSn(), null, List.of());
				SecurityContextHolder.getContext().setAuthentication(auth);

			
		}
		return loginDto;
	
	}


	@Override
	@Transactional
	public boolean logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		boolean result = false;
		
		String refreshToken = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
		    for (Cookie cookie : cookies) {
		        if ("refreshToken".equals(cookie.getName())) {
		            refreshToken = cookie.getValue();
		            break;
		        }
		    }
		}
		
		if (refreshToken != null && jwtProvider.validateToken(refreshToken)) {
		    String userMngSn = jwtProvider.getUserIdFromToken(refreshToken);

		    // 3. Token + userMngSn 조합으로 DB에서 삭제(실제로 INSERT)
		    
		    Map<String, Object> param = new HashMap<>();
		    //토큰 삭제 시점
		    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		    
			//refreshToken 발급시간 
			long longTypeIsuuedTime = jwtProvider.getIssuedAt(refreshToken);

			Timestamp issuedDt =  new Timestamp(longTypeIsuuedTime);
			
			//만료시간 구하기
			long longTypeExpireTime = jwtProvider.getIssuedAt(refreshToken);
			
			Timestamp expiresDt = new Timestamp(longTypeExpireTime);
			
			//클라이언트 ip
			String ipAddress = request.getHeader("X-Forwarded-For");
			if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
			    ipAddress = request.getRemoteAddr();
			}
			
			//접속한 브라우저 정보
			String usedBrowser = request.getHeader("User-Agent");
			
		    param.put("userMngSn", userMngSn);
		    param.put("refreshToken", refreshToken);
		    param.put("revokedDt",  currentTimestamp);
		    param.put("issuedDt",  issuedDt);
		    param.put("expiresDt",  expiresDt);
		    param.put("ipAddress", ipAddress);
		    param.put("usedBrowser", usedBrowser);

		    int resCnt = tokenMngService.deleteRefreshToken(param);  // DAO 또는 Mapper에서 delete 쿼리 수행
		    
		    if(resCnt > 0) {
		    	result = true;
		    }
		}
		
		return result;
	}

}
