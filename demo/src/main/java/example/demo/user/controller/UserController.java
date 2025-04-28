package example.demo.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import example.demo.user.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	
	
	@PostMapping("/userRegister")
	public String userRegister(@RequestBody Map<String, Object> userData) throws Exception {
		
		int res = 0;
		String resMsg = "";

		if(!ObjectUtils.isEmpty(userData)) {
				
			res = userService.RegisterUser(userData);
			
		}
		
		if(res > 0) {
			resMsg = "seccess";
		}
		
		return resMsg;
	}
	
	
	@GetMapping("/getUserData")
	public String getUserData(@RequestParam Map<String, Object> userData) {
		
		System.out.println("=============");
		System.out.println("sucees");
		System.out.println("=============");
		return "success";
	}
	
	@GetMapping("/getIdDuplicateChk")
	public Map<String, Object> getIdDuplicateChk(@RequestParam Map<String, Object> chkParam) throws Exception {
		
		Map<String, Object> res = new HashMap<String, Object>();
		
		
		res = userService.getIdDuplicateYn(chkParam);
		
			
		return res;
	}
	
	
	
}
