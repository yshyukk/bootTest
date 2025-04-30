package example.demo.config;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import example.demo.exception.CustomJwtException;
import example.demo.main.service.LoginDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Component
public class JwtProvider {
	
	//실제 프로젝트진행중에는 커밋하면 안되니까 별도로 관리해야함.
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    
    private final long accessTokenValidityInMilliseconds = 1000 * 60 * 30;  // 30분
    private final long refreshTokenValidityInMilliseconds = 1000L * 60 * 60 * 24 * 7; // 7일
    	
    //JWT 생성 (= AccessToken)
    public String generateToken(LoginDto loginDto) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenValidityInMilliseconds); //30분

        return Jwts.builder()
                .setSubject(loginDto.getUserMngSn())
                .setIssuedAt(new Date())	//생성일자
                .setExpiration(expiryDate)	//만료일자
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    //RefreshToken 발급
    public String generateRefreshToken(LoginDto loginDto) {
    	Date now = new Date();
    	Date expiryDate = new Date(now.getTime() + refreshTokenValidityInMilliseconds); // 7일
    	
    	return Jwts.builder()
    			.setSubject(loginDto.getUserMngSn())
    			.setIssuedAt(new Date())
    			.setExpiration(expiryDate)
    			.signWith(secretKey, SignatureAlgorithm.HS512)
    			.compact();
    }
    
    //토큰에서 userId추출
    public String getUserIdFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()  // << 여기서 JwtParser 완성
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    //토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()  // << 여기서 JwtParser 완성
            .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) { //토큰 마료
            throw new CustomJwtException(CustomJwtException.JwtErrorType.EXPIRED, e);

        }catch (SignatureException e) {
            // 서명 검증 실패인 경우 예외 처리
            throw new CustomJwtException(CustomJwtException.JwtErrorType.INVALID, e);
        } catch (Exception e) {
            // 그 외 다른 예외 처리
            throw new CustomJwtException(CustomJwtException.JwtErrorType.INVALID, e);
        }
    }
    
    /*생성일자 추출*/
    public long getIssuedAt(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey) // 서명 키
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getIssuedAt()
            .getTime();  // Date → timestamp(long) 변환
    }

    
    /*만료일 추출*/
	public long getExpirationDate(String token) {
	 return  Jwts.parserBuilder()
	        .setSigningKey(secretKey)  // 당신의 서명 키로 교체
	        .build()
	        .parseClaimsJws(token)
	        .getBody()
	        .getExpiration().getTime();
	}
    
    
}
