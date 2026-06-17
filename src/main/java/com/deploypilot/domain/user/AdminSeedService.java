package com.deploypilot.domain.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminSeedService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public AdminSeedService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	public boolean seed(AdminSeedProperties properties) {
		if (!properties.canSeed() || userRepository.existsByEmail(properties.email())) {
			return false;
		}

		User admin = User.create(
				properties.email(),
				passwordEncoder.encode(properties.password()),
				properties.seedName(),
				Role.ADMIN
		);
		userRepository.save(admin);
		return true;
	}
}
