package com.deploypilot.domain.release.dto;

import com.deploypilot.domain.release.GateStatus;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseStatus;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import java.time.LocalDateTime;

public record ReleaseResponse(
		Long id,
		Long serviceAppId,
		String serviceName,
		String version,
		ServiceAppEnvironment environment,
		String branch,
		String commitHash,
		LocalDateTime commitTime,
		ReleaseStatus status,
		GateStatus gateStatus,
		LocalDateTime createdAt,
		LocalDateTime ciStartedAt,
		LocalDateTime ciFinishedAt,
		LocalDateTime deployedAt,
		LocalDateTime monitoringStartedAt,
		LocalDateTime stableAt,
		LocalDateTime failedAt,
		LocalDateTime rolledBackAt
) {

	public static ReleaseResponse from(Release release) {
		return new ReleaseResponse(
				release.getId(),
				release.getServiceApp().getId(),
				release.getServiceApp().getName(),
				release.getVersion(),
				release.getEnvironment(),
				release.getBranch(),
				release.getCommitHash(),
				release.getCommitTime(),
				release.getStatus(),
				release.getGateStatus(),
				release.getCreatedAt(),
				release.getCiStartedAt(),
				release.getCiFinishedAt(),
				release.getDeployedAt(),
				release.getMonitoringStartedAt(),
				release.getStableAt(),
				release.getFailedAt(),
				release.getRolledBackAt()
		);
	}
}
