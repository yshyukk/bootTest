package example.demo.main.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import example.demo.main.service.LoginDto;

@Mapper
public interface LoginMapper {
	
	public LoginDto getLoginUserChkId(Map<String, Object> loginParam) throws Exception; 
	
	public String getPassWd(Map<String, Object> loginParam) throws Exception;
	
}
