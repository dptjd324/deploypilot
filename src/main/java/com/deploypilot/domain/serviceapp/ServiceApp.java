package com.deploypilot.domain.serviceapp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_app")
public class ServiceApp {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 100)
	private String name;

	@Column(length = 500)
	private String description;

	@Column(name = "repository_url", nullable = false, length = 500)
	private String repositoryUrl;

	@Column(name = "default_branch", nullable = false, length = 100)
	private String defaultBranch;

	@Column(name = "team_name", nullable = false, length = 100)
	private String teamName;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private ServiceAppEnvironment environment;

	@Column(name = "health_check_url", length = 500)
	private String healthCheckUrl;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private ServiceAppStatus status;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	protected ServiceApp() {
	}

	private ServiceApp(
			String name,
			String description,
			String repositoryUrl,
			String defaultBranch,
			String teamName,
			ServiceAppEnvironment environment,
			String healthCheckUrl,
			ServiceAppStatus status
	) {
		this.name = name;
		this.description = description;
		this.repositoryUrl = repositoryUrl;
		this.defaultBranch = defaultBranch;
		this.teamName = teamName;
		this.environment = environment;
		this.healthCheckUrl = healthCheckUrl;
		this.status = status;
	}

	public static ServiceApp create(
			String name,
			String description,
			String repositoryUrl,
			String defaultBranch,
			String teamName,
			ServiceAppEnvironment environment,
			String healthCheckUrl
	) {
		return new ServiceApp(
				name,
				description,
				repositoryUrl,
				defaultBranch,
				teamName,
				environment,
				healthCheckUrl,
				ServiceAppStatus.ACTIVE
		);
	}

	public void update(
			String name,
			String description,
			String repositoryUrl,
			String defaultBranch,
			String teamName,
			ServiceAppEnvironment environment,
			String healthCheckUrl,
			ServiceAppStatus status
	) {
		this.name = name;
		this.description = description;
		this.repositoryUrl = repositoryUrl;
		this.defaultBranch = defaultBranch;
		this.teamName = teamName;
		this.environment = environment;
		this.healthCheckUrl = healthCheckUrl;
		this.status = status;
	}

	@PrePersist
	void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		this.createdAt = now;
		this.updatedAt = now;
	}

	@PreUpdate
	void preUpdate() {
		this.updatedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getRepositoryUrl() {
		return repositoryUrl;
	}

	public String getDefaultBranch() {
		return defaultBranch;
	}

	public String getTeamName() {
		return teamName;
	}

	public ServiceAppEnvironment getEnvironment() {
		return environment;
	}

	public String getHealthCheckUrl() {
		return healthCheckUrl;
	}

	public ServiceAppStatus getStatus() {
		return status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
