package com.deploypilot.web.auth;

import com.deploypilot.domain.user.AuthService;
import com.deploypilot.domain.user.dto.AuthUserResponse;
import com.deploypilot.domain.user.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public AuthUserResponse login(
			@Valid @RequestBody LoginRequest request,
			HttpServletRequest httpRequest,
			HttpServletResponse httpResponse
	) {
		return authService.login(request, httpRequest, httpResponse);
	}

	@PostMapping("/logout")
	public AuthUserResponse logout(HttpServletRequest request) {
		return authService.logout(request);
	}

	@GetMapping("/me")
	public AuthUserResponse me(Authentication authentication) {
		return authService.me(authentication);
	}
}
