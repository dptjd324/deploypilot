package com.deploypilot.domain.smoke;

import com.deploypilot.domain.release.Release;
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
@Table(name = "smoke_test_result")
public class SmokeTestResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "release_id", nullable = false)
	private Release release;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "smoke_test_id", nullable = false)
	private SmokeTest smokeTest;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private SmokeTestResultStatus status;

	@Column(name = "actual_status")
	private Integer actualStatus;

	@Column(name = "response_time_ms")
	private Long responseTimeMs;

	@Column(name = "error_message", length = 500)
	private String errorMessage;

	@Column(name = "executed_at", nullable = false)
	private LocalDateTime executedAt;

	protected SmokeTestResult() {
	}

	private SmokeTestResult(
			Release release,
			SmokeTest smokeTest,
			SmokeTestResultStatus status,
			Integer actualStatus,
			Long responseTimeMs,
			String errorMessage
	) {
		this.release = release;
		this.smokeTest = smokeTest;
		this.status = status;
		this.actualStatus = actualStatus;
		this.responseTimeMs = responseTimeMs;
		this.errorMessage = errorMessage;
	}

	public static SmokeTestResult create(
			Release release,
			SmokeTest smokeTest,
			SmokeTestResultStatus status,
			Integer actualStatus,
			Long responseTimeMs,
			String errorMessage
	) {
		return new SmokeTestResult(release, smokeTest, status, actualStatus, responseTimeMs, errorMessage);
	}

	@PrePersist
	void prePersist() {
		this.executedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public Release getRelease() {
		return release;
	}

	public SmokeTest getSmokeTest() {
		return smokeTest;
	}

	public SmokeTestResultStatus getStatus() {
		return status;
	}

	public Integer getActualStatus() {
		return actualStatus;
	}

	public Long getResponseTimeMs() {
		return responseTimeMs;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public LocalDateTime getExecutedAt() {
		return executedAt;
	}
}
