package example.demo.config;

import java.util.Date;

import org.springframework.stereotype.Component;

import example.demo.main.service.LoginDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {

    private final String secretKey = "01062163780"; // 보안적으로 외부 설정으로 빼는 게 좋음
    private final long EXPIRATION_TIME = 1000 * 60 * 60; // 1시간
    	
    //JWT 생성
    public String generateToken(LoginDto loginDto) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(loginDto.getUserId())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}
