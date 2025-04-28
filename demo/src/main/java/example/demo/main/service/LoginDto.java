package example.demo.main.service;

public class LoginDto {
	
	private String userId;
	private String passwd;
	private String token;
	private String userName;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserAuth() {
		return userAuth;
	}
	public void setUserAuth(String userAuth) {
		this.userAuth = userAuth;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	private String email;
	private String userAuth;
	private String phoneNum;
	
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
