package example.demo.user.service.Impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import example.demo.user.dao.UserMapper;
import example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserMapper userMapper;

	//Spring security Bcrypt hash encodeer
	private final PasswordEncoder passwordEncoder;

	 
	@Override
	public int RegisterUser(Map<String, Object> userInfo) throws Exception {
		 
		
		//비밀번호 해시값으로 encode
		//로그인 시 비밀번호 확인은 passwordEncoder.matches(rawPassword, encodedPassword)
		String encodePasswd = passwordEncoder.encode(userInfo.get("passwd").toString());
		userInfo.put("passwd", encodePasswd);
		
		int res = userMapper.registerUser(userInfo);
		
		return res;
	}


	@Override
	public Map<String, Object> getUserData(Map<String, Object> userInfo) throws Exception {
		Map<String, Object> userData = userMapper.getUserData(userInfo);
		return userData;
	}


	@Override
	public Map<String, Object> getIdDuplicateYn(Map<String, Object> chkParam) throws Exception {
		
		Map<String, Object> chkData = userMapper.getIdDuplicateYn(chkParam);
		
		return chkData;
	}

}
