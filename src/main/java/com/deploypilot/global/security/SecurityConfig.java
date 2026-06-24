package com.deploypilot.global.security;

import com.deploypilot.domain.user.User;
import com.deploypilot.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final boolean permitAllRequests;

	public SecurityConfig(@Value("${deploypilot.security.permit-all:false}") boolean permitAllRequests) {
		this.permitAllRequests = permitAllRequests;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> {
					if (permitAllRequests) {
						auth.anyRequest().permitAll();
						return;
					}

					auth.requestMatchers("/actuator/health").permitAll()
							.requestMatchers(HttpMethod.POST, "/api/integrations/github-actions/runs").permitAll()
							.anyRequest().authenticated();
				})
				.httpBasic(Customizer.withDefaults())
				.build();
	}

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository) {
		return email -> {
			User user = userRepository.findByEmail(email)
					.orElseThrow(() -> new UsernameNotFoundException("User not found"));

			return org.springframework.security.core.userdetails.User
					.withUsername(user.getEmail())
					.password(user.getPassword())
					.roles(user.getRole().name())
					.build();
		};
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
