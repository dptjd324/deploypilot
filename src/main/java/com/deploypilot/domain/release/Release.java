package com.deploypilot.domain.release;

import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import com.deploypilot.domain.gate.GateResult;
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
@Table(name = "release")
public class Release {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "service_app_id", nullable = false)
	private ServiceApp serviceApp;

	@Column(nullable = false, length = 100)
	private String version;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private ServiceAppEnvironment environment;

	@Column(nullable = false, length = 100)
	private String branch;

	@Column(name = "commit_hash", nullable = false, length = 100)
	private String commitHash;

	@Column(name = "commit_time")
	private LocalDateTime commitTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private ReleaseStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "gate_status", nullable = false, length = 50)
	private GateStatus gateStatus;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "ci_started_at")
	private LocalDateTime ciStartedAt;

	@Column(name = "ci_finished_at")
	private LocalDateTime ciFinishedAt;

	@Column(name = "deployed_at")
	private LocalDateTime deployedAt;

	@Column(name = "monitoring_started_at")
	private LocalDateTime monitoringStartedAt;

	@Column(name = "stable_at")
	private LocalDateTime stableAt;

	@Column(name = "failed_at")
	private LocalDateTime failedAt;

	@Column(name = "rolled_back_at")
	private LocalDateTime rolledBackAt;

	protected Release() {
	}

	private Release(
			ServiceApp serviceApp,
			String version,
			ServiceAppEnvironment environment,
			String branch,
			String commitHash,
			LocalDateTime commitTime
	) {
		this.serviceApp = serviceApp;
		this.version = version;
		this.environment = environment;
		this.branch = branch;
		this.commitHash = commitHash;
		this.commitTime = commitTime;
		this.status = ReleaseStatus.CREATED;
		this.gateStatus = GateStatus.PENDING;
	}

	public static Release create(
			ServiceApp serviceApp,
			String version,
			ServiceAppEnvironment environment,
			String branch,
			String commitHash,
			LocalDateTime commitTime
	) {
		return new Release(serviceApp, version, environment, branch, commitHash, commitTime);
	}

	public void approve() {
		if (this.status != ReleaseStatus.CREATED && this.status != ReleaseStatus.CI_SUCCESS) {
			throw new IllegalStateException("Invalid release status transition");
		}
		this.status = ReleaseStatus.APPROVED;
	}

	public void deploy() {
		requireStatus(ReleaseStatus.APPROVED);
		this.status = ReleaseStatus.DEPLOYED;
		this.deployedAt = LocalDateTime.now();
	}

	public void rollback() {
		if (this.status == ReleaseStatus.ROLLED_BACK) {
			throw new IllegalStateException("Release is already rolled back");
		}
		this.status = ReleaseStatus.ROLLED_BACK;
		this.rolledBackAt = LocalDateTime.now();
	}

	public void applyCiResult(LocalDateTime startedAt, LocalDateTime finishedAt, boolean completed, boolean successful) {
		this.ciStartedAt = startedAt;
		this.ciFinishedAt = finishedAt;

		if (!completed) {
			this.status = ReleaseStatus.CI_RUNNING;
			return;
		}

		if (successful) {
			this.status = ReleaseStatus.CI_SUCCESS;
			return;
		}

		this.status = ReleaseStatus.CI_FAILED;
		this.failedAt = finishedAt != null ? finishedAt : LocalDateTime.now();
	}

	public void applyGateResult(GateResult result) {
		this.gateStatus = GateStatus.valueOf(result.name());
	}

	private void requireStatus(ReleaseStatus requiredStatus) {
		if (this.status != requiredStatus) {
			throw new IllegalStateException("Invalid release status transition");
		}
	}

	@PrePersist
	void prePersist() {
		this.createdAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public ServiceApp getServiceApp() {
		return serviceApp;
	}

	public String getVersion() {
		return version;
	}

	public ServiceAppEnvironment getEnvironment() {
		return environment;
	}

	public String getBranch() {
		return branch;
	}

	public String getCommitHash() {
		return commitHash;
	}

	public LocalDateTime getCommitTime() {
		return commitTime;
	}

	public ReleaseStatus getStatus() {
		return status;
	}

	public GateStatus getGateStatus() {
		return gateStatus;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getCiStartedAt() {
		return ciStartedAt;
	}

	public LocalDateTime getCiFinishedAt() {
		return ciFinishedAt;
	}

	public LocalDateTime getDeployedAt() {
		return deployedAt;
	}

	public LocalDateTime getMonitoringStartedAt() {
		return monitoringStartedAt;
	}

	public LocalDateTime getStableAt() {
		return stableAt;
	}

	public LocalDateTime getFailedAt() {
		return failedAt;
	}

	public LocalDateTime getRolledBackAt() {
		return rolledBackAt;
	}
}
