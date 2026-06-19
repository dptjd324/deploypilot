package com.deploypilot.domain.serviceapp.dto;

import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import com.deploypilot.domain.serviceapp.ServiceAppStatus;
import java.time.LocalDateTime;

public record ServiceAppResponse(
		Long id,
		String name,
		String description,
		String repositoryUrl,
		String defaultBranch,
		String teamName,
		ServiceAppEnvironment environment,
		String healthCheckUrl,
		ServiceAppStatus status,
		LocalDateTime createdAt,
		LocalDateTime updatedAt
) {

	public static ServiceAppResponse from(ServiceApp serviceApp) {
		return new ServiceAppResponse(
				serviceApp.getId(),
				serviceApp.getName(),
				serviceApp.getDescription(),
				serviceApp.getRepositoryUrl(),
				serviceApp.getDefaultBranch(),
				serviceApp.getTeamName(),
				serviceApp.getEnvironment(),
				serviceApp.getHealthCheckUrl(),
				serviceApp.getStatus(),
				serviceApp.getCreatedAt(),
				serviceApp.getUpdatedAt()
		);
	}
}
