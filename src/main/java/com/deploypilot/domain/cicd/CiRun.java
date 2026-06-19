package com.deploypilot.domain.cicd;

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
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "ci_run")
public class CiRun {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "release_id", nullable = false)
	private Release release;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private CiProvider provider;

	@Column(name = "workflow_name", nullable = false, length = 200)
	private String workflowName;

	@Column(name = "run_id", length = 100)
	private String runId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private CiRunStatus status;

	@Column(name = "total_tests")
	private Integer totalTests;

	@Column(name = "passed_tests")
	private Integer passedTests;

	@Column(name = "failed_tests")
	private Integer failedTests;

	@Column(precision = 5, scale = 2)
	private BigDecimal coverage;

	@Column(name = "started_at")
	private LocalDateTime startedAt;

	@Column(name = "finished_at")
	private LocalDateTime finishedAt;

	@Column(name = "duration_seconds")
	private Long durationSeconds;

	@Column(name = "log_url", length = 500)
	private String logUrl;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	protected CiRun() {
	}

	private CiRun(
			Release release,
			CiProvider provider,
			String workflowName,
			String runId,
			CiRunStatus status,
			Integer totalTests,
			Integer passedTests,
			Integer failedTests,
			BigDecimal coverage,
			LocalDateTime startedAt,
			LocalDateTime finishedAt,
			String logUrl
	) {
		this.release = release;
		this.provider = provider;
		this.workflowName = workflowName;
		this.runId = runId;
		this.status = status;
		this.totalTests = totalTests;
		this.passedTests = passedTests;
		this.failedTests = failedTests;
		this.coverage = coverage;
		this.startedAt = startedAt;
		this.finishedAt = finishedAt;
		this.durationSeconds = calculateDurationSeconds(startedAt, finishedAt);
		this.logUrl = logUrl;
	}

	public static CiRun create(
			Release release,
			CiProvider provider,
			String workflowName,
			String runId,
			CiRunStatus status,
			Integer totalTests,
			Integer passedTests,
			Integer failedTests,
			BigDecimal coverage,
			LocalDateTime startedAt,
			LocalDateTime finishedAt,
			String logUrl
	) {
		return new CiRun(
				release,
				provider,
				workflowName,
				runId,
				status,
				totalTests,
				passedTests,
				failedTests,
				coverage,
				startedAt,
				finishedAt,
				logUrl
		);
	}

	public boolean isCompleted() {
		return status != CiRunStatus.IN_PROGRESS;
	}

	public boolean isSuccessful() {
		return status == CiRunStatus.SUCCESS;
	}

	@PrePersist
	void prePersist() {
		this.createdAt = LocalDateTime.now();
	}

	private Long calculateDurationSeconds(LocalDateTime startedAt, LocalDateTime finishedAt) {
		if (startedAt == null || finishedAt == null) {
			return null;
		}
		return Duration.between(startedAt, finishedAt).getSeconds();
	}

	public Long getId() {
		return id;
	}

	public Release getRelease() {
		return release;
	}

	public CiProvider getProvider() {
		return provider;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public String getRunId() {
		return runId;
	}

	public CiRunStatus getStatus() {
		return status;
	}

	public Integer getTotalTests() {
		return totalTests;
	}

	public Integer getPassedTests() {
		return passedTests;
	}

	public Integer getFailedTests() {
		return failedTests;
	}

	public BigDecimal getCoverage() {
		return coverage;
	}

	public LocalDateTime getStartedAt() {
		return startedAt;
	}

	public LocalDateTime getFinishedAt() {
		return finishedAt;
	}

	public Long getDurationSeconds() {
		return durationSeconds;
	}

	public String getLogUrl() {
		return logUrl;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
}
