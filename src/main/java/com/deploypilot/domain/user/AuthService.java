package com.deploypilot.domain.user;

import com.deploypilot.domain.user.dto.AuthUserResponse;
import com.deploypilot.domain.user.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

	public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
	}

	@Transactional(readOnly = true)
	public AuthUserResponse login(LoginRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		Authentication authentication = authenticationManager.authenticate(
				UsernamePasswordAuthenticationToken.unauthenticated(request.email(), request.password())
		);
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		SecurityContextHolder.setContext(context);
		securityContextRepository.saveContext(context, httpRequest, httpResponse);
		return findByEmail(authentication.getName());
	}

	@Transactional(readOnly = true)
	public AuthUserResponse me(Authentication authentication) {
		if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
			return AuthUserResponse.anonymous();
		}
		return findByEmail(authentication.getName());
	}

	public AuthUserResponse logout(HttpServletRequest request) {
		SecurityContextHolder.clearContext();
		var session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return AuthUserResponse.anonymous();
	}

	private AuthUserResponse findByEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		return AuthUserResponse.from(user);
	}
}
