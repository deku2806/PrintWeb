package Admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.cors() // Enable CORS
				.and().csrf().disable() // Disable CSRF for stateless APIs
				.authorizeHttpRequests()
				.requestMatchers("/api/auth/**", "/api/team/**", "api/project/**", "api/customer/**", "api/resource/**",
						"api/design/**", "api/notification/**","api/print/**","api/bill/**","api/delivery/**")
				.permitAll() 
				.anyRequest().authenticated()
				.and().formLogin().disable() 
				.httpBasic().disable(); 
		return http.build();
		
	}
}