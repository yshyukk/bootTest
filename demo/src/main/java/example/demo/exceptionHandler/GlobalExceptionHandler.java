package example.demo.exceptionHandler;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import example.demo.exception.CustomJwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@Autowired
	MessageSource messageSource;
	
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentials(BadCredentialsException e) {
    	
    	String errMsg = messageSource.getMessage(e.getMessage(), null, Locale.KOREA);
    	
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("errorMsg", errMsg));
    }
    
    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<?> handleCustomJwtException(CustomJwtException e) {
        String errMsg = e.getErrorType().getMessage();
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("errorMsg", errMsg));
    }
}
