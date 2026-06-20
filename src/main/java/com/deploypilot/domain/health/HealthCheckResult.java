package com.deploypilot.domain.health;

import com.deploypilot.domain.release.Release;
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
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "health_check_result")
public class HealthCheckResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "release_id", nullable = false)
	private Release release;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "service_app_id", nullable = false)
	private ServiceApp serviceApp;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private HealthCheckStatus status;

	@Column(name = "http_status")
	private Integer httpStatus;

	@Column(name = "response_time_ms")
	private Long responseTimeMs;

	@Column(name = "response_body", columnDefinition = "TEXT")
	private String responseBody;

	@Column(name = "error_message", length = 500)
	private String errorMessage;

	@Column(name = "checked_at", nullable = false)
	private LocalDateTime checkedAt;

	protected HealthCheckResult() {
	}

	private HealthCheckResult(
			Release release,
			ServiceApp serviceApp,
			HealthCheckStatus status,
			Integer httpStatus,
			Long responseTimeMs,
			String responseBody,
			String errorMessage
	) {
		this.release = release;
		this.serviceApp = serviceApp;
		this.status = status;
		this.httpStatus = httpStatus;
		this.responseTimeMs = responseTimeMs;
		this.responseBody = responseBody;
		this.errorMessage = errorMessage;
	}

	public static HealthCheckResult success(
			Release release,
			ServiceApp serviceApp,
			int httpStatus,
			long responseTimeMs,
			String responseBody
	) {
		return new HealthCheckResult(
				release,
				serviceApp,
				HealthCheckStatus.PASS,
				httpStatus,
				responseTimeMs,
				responseBody,
				null
		);
	}

	public static HealthCheckResult failure(
			Release release,
			ServiceApp serviceApp,
			Integer httpStatus,
			Long responseTimeMs,
			String responseBody,
			String errorMessage
	) {
		return new HealthCheckResult(
				release,
				serviceApp,
				HealthCheckStatus.FAIL,
				httpStatus,
				responseTimeMs,
				responseBody,
				errorMessage
		);
	}

	@PrePersist
	void prePersist() {
		this.checkedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public Release getRelease() {
		return release;
	}

	public ServiceApp getServiceApp() {
		return serviceApp;
	}

	public HealthCheckStatus getStatus() {
		return status;
	}

	public Integer getHttpStatus() {
		return httpStatus;
	}

	public Long getResponseTimeMs() {
		return responseTimeMs;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public LocalDateTime getCheckedAt() {
		return checkedAt;
	}
}
