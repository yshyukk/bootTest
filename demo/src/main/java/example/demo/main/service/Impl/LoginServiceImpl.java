package example.demo.main.service.Impl;

import java.util.Map;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import example.demo.config.JwtProvider;
import example.demo.main.dao.LoginMapper;
import example.demo.main.service.LoginDto;
import example.demo.main.service.LoginService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{
	
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final LoginMapper loginMapper;
	
	
	@Override
	public LoginDto doLogin(Map<String, Object> loginParam) throws Exception {
		
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
			
			if(loginDto == null ||  (matchesYn != true )) {
				
				//Security exception 예외처리 (경고창 필요할듯) -- 이걸 타도 아래 로직 수행함..
				throw new BadCredentialsException("Invalid username or password");
			}
			//  오류메시지 : The signing key's size is 64 bits which is not secure enough for the HS512 algorithm.  The JWT JWA Specification (RFC 7518, Section 3.2) states that keys used with HS512 MUST have a size >= 512 bits (the key size must be greater than or equal to the hash output size).  Consider using the io.jsonwebtoken.security.Keys class's 'secretKeyFor(SignatureAlgorithm.HS512)' method to create a key guaranteed to be secure enough for HS512.  See https://tools.ietf.org/html/rfc7518#section-3.2 for more information.
			loginDto.setToken(jwtProvider.generateToken(loginDto));
		}
		return loginDto;
	
	}

}
