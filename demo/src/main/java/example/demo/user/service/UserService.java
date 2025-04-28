package example.demo.user.service;

import java.util.Map;

public interface UserService {
	
	int RegisterUser(Map<String, Object> userInfo) throws Exception;
	
	Map<String, Object> getUserData(Map<String, Object> userInfo) throws Exception;
	
	Map<String, Object> getIdDuplicateYn(Map<String, Object> userInfo) throws Exception;
}
