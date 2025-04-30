package example.demo.token.service;

import java.util.Map;

import example.demo.main.service.LoginDto;

public interface TokenMngService {
	
	int refreshTokenCreateInfo (Map<String, Object> tokenInfo) throws Exception;
	
	LoginDto findByUserInfoIsRevokedFalseToken (Map<String, Object> param) throws Exception;
	
	int deleteRefreshToken (Map<String, Object> param) throws Exception;
	
}
