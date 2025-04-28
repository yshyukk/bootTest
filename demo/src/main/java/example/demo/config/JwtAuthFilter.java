package example.demo.config;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter{
	
	private final JwtProvider jwtProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
			String token = resolveToken(request);
			
			 if (token != null && jwtProvider.validateToken(token)) {
		            String username = jwtProvider.getUsernameFromToken(token);

		            UsernamePasswordAuthenticationToken auth =
		                new UsernamePasswordAuthenticationToken(username, null, List.of());

		            SecurityContextHolder.getContext().setAuthentication(auth);
		        }

		        filterChain.doFilter(request, response);
		
		
	}
	
	private String resolveToken(HttpServletRequest request) {
		
		String bearer = request.getHeader("Authorization");
			
		if(bearer != null && bearer.startsWith("Bearer ")) {
			return bearer.substring(7);
		}
		
		return null;
	}
	
	
}
