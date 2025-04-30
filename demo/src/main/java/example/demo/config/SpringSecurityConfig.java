package example.demo.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import example.demo.token.service.TokenMngService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {
	
		private final JwtProvider jwtProvider;
		private final TokenMngService tokenMngService;
		
	    @Bean
	    public JwtAuthFilter jwtAuthFilter() {
	        return new JwtAuthFilter(jwtProvider, tokenMngService);
	    }
		
		@Bean("customSecurityFilterChain")
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	        http
	            .cors(Customizer.withDefaults()) // 👉 CORS 허용
	            .csrf(csrf -> csrf.disable()) // 👉 CSRF 끔
	            .authorizeHttpRequests(auth -> auth
            		.requestMatchers("/api/**").permitAll()
	                .anyRequest().permitAll() // 모든 요청 허용
	            )
	            .formLogin(AbstractHttpConfigurer::disable)  // ✅ 로그인 비활성화
	            .httpBasic(AbstractHttpConfigurer::disable)  // ✅ Basic Auth 비활성화
	            .exceptionHandling(exception -> exception
	                .authenticationEntryPoint((request, response, authException) -> {
	              
	                	// ✅ 인증 실패 시 401 응답 보내기
	                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	                })
	            ).sessionManagement(session -> session
	            		.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            ).addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
                        
	        return http.build();
	    }

	    @Bean
	    public CorsConfigurationSource corsConfigurationSource() {
	        CorsConfiguration configuration = new CorsConfiguration();
	        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
	        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
	        configuration.setAllowedHeaders(List.of("*"));
	        configuration.setAllowCredentials(true);

	        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	        
	        source.registerCorsConfiguration("/**", configuration);  // API 경로에 대한 CORS
	        
	        return source;
	    }
	    
	    //bcrypt 암호화
	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    } 
	    
	   
}
