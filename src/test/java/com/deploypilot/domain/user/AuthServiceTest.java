package com.deploypilot.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private UserRepository userRepository;

	@Test
	void meReturnsAuthenticatedUserWhenPrincipalExists() {
		AuthService service = new AuthService(null, userRepository);
		User user = user();

		when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(user));

		var response = service.me(new TestingAuthenticationToken("admin@example.com", null, "ROLE_ADMIN"));

		assertThat(response.authenticated()).isTrue();
		assertThat(response.id()).isEqualTo(1L);
		assertThat(response.email()).isEqualTo("admin@example.com");
		assertThat(response.role()).isEqualTo(Role.ADMIN.name());
	}

	@Test
	void meReturnsAnonymousWhenAuthenticationIsMissing() {
		AuthService service = new AuthService(null, userRepository);

		var response = service.me(null);

		assertThat(response.authenticated()).isFalse();
		assertThat(response.email()).isNull();
		assertThat(response.name()).isEqualTo("익명 사용자");
	}

	private User user() {
		User user = User.create("admin@example.com", "encoded", "Admin", Role.ADMIN);
		ReflectionTestUtils.setField(user, "id", 1L);
		return user;
	}
}
