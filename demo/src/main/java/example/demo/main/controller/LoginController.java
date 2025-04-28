package example.demo.main.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.demo.main.service.LoginDto;
import example.demo.main.service.LoginService;

@RestController
@RequestMapping("/api")
public class LoginController {
	
	@Autowired
	LoginService loginService;
	
	@PostMapping("/doLogin")
	public ResponseEntity<LoginDto> doLogin(@RequestBody Map<String, Object> loginParam) throws Exception{
		
		Map<String, Object> res = new HashMap<String, Object>();
		
		LoginDto LoginConfDto = loginService.doLogin(loginParam);
		
		
		return ResponseEntity.ok().body(LoginConfDto);
	}

}
