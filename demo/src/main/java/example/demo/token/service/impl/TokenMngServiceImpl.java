package example.demo.token.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import example.demo.main.service.LoginDto;
import example.demo.token.dao.TokenMngMapper;
import example.demo.token.service.TokenMngService;


@Service
public class TokenMngServiceImpl implements TokenMngService{
	
	@Autowired
	TokenMngMapper tokenMngMapper;
	
	@Override
	@Transactional
	public int refreshTokenCreateInfo(Map<String, Object> tokenInfo) throws Exception {
		
		if(!ObjectUtils.isEmpty(tokenInfo)){
			
			tokenMngMapper.refreshTokenCreateInfo(tokenInfo);
			
		}
		
		return 0;
	}

	@Override
	@Transactional(readOnly = true)
	public LoginDto findByUserInfoIsRevokedFalseToken(Map<String, Object> param) throws Exception {
		
		LoginDto loginDto = new LoginDto();
		
		if(!ObjectUtils.isEmpty(param)) {
			loginDto = tokenMngMapper.findByUserInfoIsRevokedFalseToken(param);
		}else {
			loginDto = null;
		}
		return loginDto;
	}

	@Override
	@Transactional
	public int deleteRefreshToken(Map<String, Object> param) throws Exception {
		
		int res = 0;
		
		if(!ObjectUtils.isEmpty(param)) {
			res = tokenMngMapper.deleteRefreshToken(param);
		}
		
		return res;
	}

}
