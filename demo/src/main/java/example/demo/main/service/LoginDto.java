package example.demo.main.service;

import java.sql.Timestamp;

public class LoginDto {
	
	private String userId;
	private String passwd;
	private String userMngSn;
	private String refreshToken;
	private String userName;
	private String email;
	private String userAuth;
	private String phoneNum;
	private String delYn;
	private String useYn;
	private Timestamp  expiresDt;
	private Timestamp  issuedDt;
	
	public String getUserMngSn() {
		return userMngSn;
	}
	public void setUserMngSn(String userMngSn) {
		this.userMngSn = userMngSn;
	}
	public String getDelYn() {
		return delYn;
	}
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
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
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String token) {
		this.refreshToken = token;
	}
	public Timestamp getExpiresDt() {
		return expiresDt;
	}
	public void setExpiresDt(Timestamp expiresDt) {
		this.expiresDt = expiresDt;
	}
	public Timestamp getIssuedDt() {
		return issuedDt;
	}
	public void setIssuedDt(Timestamp issuedDt) {
		this.issuedDt = issuedDt;
	}
}
