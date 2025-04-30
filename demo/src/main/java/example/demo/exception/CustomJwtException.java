package example.demo.exception;

import io.jsonwebtoken.ExpiredJwtException;

public class CustomJwtException extends RuntimeException{
	
    // serialVersionUID 추가
    private static final long serialVersionUID = 1L;
    
    private final JwtErrorType errorType;
    private final Throwable throwable; // Exception에서 Throwable로 수정


    
    // JwtErrorType과 ExpiredJwtException을 받는 생성자 추가
    public CustomJwtException(JwtErrorType errorType, Throwable throwable) {
        super(errorType.getMessage(), throwable);  // super()를 호출하여 메시지와 예외 객체 전달
        this.errorType = errorType;
		this.throwable  = throwable;
    }

    public JwtErrorType getErrorType() {
        return errorType;
    }
    
    public Throwable getThrowable() {
        return throwable;
    }

    public enum JwtErrorType {
        EXPIRED("토큰이 만료되었습니다."),
        INVALID("유효하지 않은 토큰입니다."),
        UNSUPPORTED("지원되지 않는 형식의 토큰입니다."),
        EMPTY("토큰이 없습니다.");

        private final String message;

        JwtErrorType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
