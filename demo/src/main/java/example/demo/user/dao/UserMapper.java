package example.demo.user.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
	
	int registerUser(Map<String, Object> param);
	
	int updateUser(Map<String, Object> param);
	
	Map<String, Object> getUserData(Map<String, Object> param);
	
	//회원가입 - 아이디 중복체크
	Map<String, Object> getIdDuplicateYn(Map<String, Object> param);
	
	
	
}
