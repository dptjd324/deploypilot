package com.deploypilot.domain.smoke;

import com.deploypilot.domain.serviceapp.ServiceApp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "smoke_test")
public class SmokeTest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "service_app_id", nullable = false)
	private ServiceApp serviceApp;

	@Column(nullable = false, length = 100)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private SmokeTestMethod method;

	@Column(nullable = false, length = 500)
	private String path;

	@Column(name = "expected_status", nullable = false)
	private int expectedStatus;

	@Column(name = "max_response_time_ms", nullable = false)
	private long maxResponseTimeMs;

	@Column(nullable = false)
	private boolean required;

	@Column(nullable = false)
	private boolean enabled;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	protected SmokeTest() {
	}

	private SmokeTest(
			ServiceApp serviceApp,
			String name,
			SmokeTestMethod method,
			String path,
			int expectedStatus,
			long maxResponseTimeMs,
			boolean required,
			boolean enabled
	) {
		this.serviceApp = serviceApp;
		this.name = name;
		this.method = method;
		this.path = path;
		this.expectedStatus = expectedStatus;
		this.maxResponseTimeMs = maxResponseTimeMs;
		this.required = required;
		this.enabled = enabled;
	}

	public static SmokeTest create(
			ServiceApp serviceApp,
			String name,
			SmokeTestMethod method,
			String path,
			int expectedStatus,
			long maxResponseTimeMs,
			boolean required,
			boolean enabled
	) {
		return new SmokeTest(serviceApp, name, method, path, expectedStatus, maxResponseTimeMs, required, enabled);
	}

	public void update(
			String name,
			SmokeTestMethod method,
			String path,
			int expectedStatus,
			long maxResponseTimeMs,
			boolean required,
			boolean enabled
	) {
		this.name = name;
		this.method = method;
		this.path = path;
		this.expectedStatus = expectedStatus;
		this.maxResponseTimeMs = maxResponseTimeMs;
		this.required = required;
		this.enabled = enabled;
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

	public ServiceApp getServiceApp() {
		return serviceApp;
	}

	public String getName() {
		return name;
	}

	public SmokeTestMethod getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public int getExpectedStatus() {
		return expectedStatus;
	}

	public long getMaxResponseTimeMs() {
		return maxResponseTimeMs;
	}

	public boolean isRequired() {
		return required;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
