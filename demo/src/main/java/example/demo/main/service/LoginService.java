package example.demo.main.service;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface LoginService {
	
	public LoginDto doLogin(Map<String, Object> loginParam, HttpServletResponse response, HttpServletRequest request) throws Exception; 
	
	public boolean logout(HttpServletRequest request, HttpServletResponse response) throws Exception; 
	
}
