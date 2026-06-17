package com.deploypilot.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AdminSeedServiceTest {

	@Mock
	private UserRepository userRepository;

	private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Test
	void seedCreatesAdminWhenCredentialsAreConfigured() {
		AdminSeedService service = new AdminSeedService(userRepository, passwordEncoder);
		AdminSeedProperties properties = new AdminSeedProperties(
				"admin@example.com",
				"plain-password",
				"Admin"
		);

		when(userRepository.existsByEmail("admin@example.com")).thenReturn(false);

		boolean seeded = service.seed(properties);

		ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
		verify(userRepository).save(captor.capture());
		User saved = captor.getValue();

		assertThat(seeded).isTrue();
		assertThat(saved.getEmail()).isEqualTo("admin@example.com");
		assertThat(saved.getName()).isEqualTo("Admin");
		assertThat(saved.getRole()).isEqualTo(Role.ADMIN);
		assertThat(passwordEncoder.matches("plain-password", saved.getPassword())).isTrue();
	}

	@Test
	void seedSkipsWhenCredentialsAreMissing() {
		AdminSeedService service = new AdminSeedService(userRepository, passwordEncoder);
		AdminSeedProperties properties = new AdminSeedProperties(null, null, null);

		boolean seeded = service.seed(properties);

		assertThat(seeded).isFalse();
		verify(userRepository, never()).save(any());
	}

	@Test
	void seedSkipsWhenAdminAlreadyExists() {
		AdminSeedService service = new AdminSeedService(userRepository, passwordEncoder);
		AdminSeedProperties properties = new AdminSeedProperties(
				"admin@example.com",
				"plain-password",
				null
		);

		when(userRepository.existsByEmail("admin@example.com")).thenReturn(true);

		boolean seeded = service.seed(properties);

		assertThat(seeded).isFalse();
		verify(userRepository, never()).save(any());
	}
}
