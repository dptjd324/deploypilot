package com.deploypilot.domain.user.dto;

import com.deploypilot.domain.user.User;

public record AuthUserResponse(
		boolean authenticated,
		Long id,
		String email,
		String name,
		String role
) {

	public static AuthUserResponse anonymous() {
		return new AuthUserResponse(false, null, null, "익명 사용자", null);
	}

	public static AuthUserResponse from(User user) {
		return new AuthUserResponse(
				true,
				user.getId(),
				user.getEmail(),
				user.getName(),
				user.getRole().name()
		);
	}
}
