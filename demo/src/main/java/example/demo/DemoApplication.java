package example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication

public class DemoApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
