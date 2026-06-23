package com.deploypilot.domain.rollback.dto;

import com.deploypilot.domain.release.ReleaseStatus;
import com.deploypilot.domain.rollback.RollbackRecord;
import java.time.LocalDateTime;

public record RollbackResponse(
		Long id,
		Long releaseId,
		String targetVersion,
		String reason,
		Long createdBy,
		String createdByEmail,
		String createdByName,
		ReleaseStatus releaseStatus,
		LocalDateTime createdAt
) {

	public static RollbackResponse from(RollbackRecord record) {
		return new RollbackResponse(
				record.getId(),
				record.getRelease().getId(),
				record.getTargetVersion(),
				record.getReason(),
				record.getCreatedBy().getId(),
				record.getCreatedBy().getEmail(),
				record.getCreatedBy().getName(),
				record.getRelease().getStatus(),
				record.getCreatedAt()
		);
	}
}
