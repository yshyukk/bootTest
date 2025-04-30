package example.demo.token.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import example.demo.main.service.LoginDto;

@Mapper
public interface TokenMngMapper {
	
	int refreshTokenCreateInfo(Map<String, Object> tokenInfo);

	LoginDto findByUserInfoIsRevokedFalseToken(Map<String, Object> param);
	
	int deleteRefreshToken(Map<String, Object> param);
}
