package com.deploypilot.domain.cicd.dto;

import com.deploypilot.domain.cicd.CiProvider;
import com.deploypilot.domain.cicd.CiRun;
import com.deploypilot.domain.cicd.CiRunStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CiRunResponse(
		Long id,
		Long releaseId,
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
		Long durationSeconds,
		String logUrl,
		LocalDateTime createdAt
) {

	public static CiRunResponse from(CiRun ciRun) {
		return new CiRunResponse(
				ciRun.getId(),
				ciRun.getRelease().getId(),
				ciRun.getProvider(),
				ciRun.getWorkflowName(),
				ciRun.getRunId(),
				ciRun.getStatus(),
				ciRun.getTotalTests(),
				ciRun.getPassedTests(),
				ciRun.getFailedTests(),
				ciRun.getCoverage(),
				ciRun.getStartedAt(),
				ciRun.getFinishedAt(),
				ciRun.getDurationSeconds(),
				ciRun.getLogUrl(),
				ciRun.getCreatedAt()
		);
	}
}
