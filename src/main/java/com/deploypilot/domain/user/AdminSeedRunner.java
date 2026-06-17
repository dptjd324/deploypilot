package com.deploypilot.domain.user;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(AdminSeedProperties.class)
public class AdminSeedRunner implements ApplicationRunner {

	private final AdminSeedService adminSeedService;
	private final AdminSeedProperties adminSeedProperties;

	public AdminSeedRunner(AdminSeedService adminSeedService, AdminSeedProperties adminSeedProperties) {
		this.adminSeedService = adminSeedService;
		this.adminSeedProperties = adminSeedProperties;
	}

	@Override
	public void run(ApplicationArguments args) {
		adminSeedService.seed(adminSeedProperties);
	}
}
