package example.demo.main.service;

import java.util.Map;

public interface LoginService {
	
	public LoginDto doLogin(Map<String, Object> loginParam) throws Exception; 
}
