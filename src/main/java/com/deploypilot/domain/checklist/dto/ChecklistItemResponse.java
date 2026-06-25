package com.deploypilot.domain.checklist.dto;

import com.deploypilot.domain.checklist.ChecklistItem;
import com.deploypilot.domain.checklist.ChecklistPhase;
import java.time.LocalDateTime;

public record ChecklistItemResponse(
		Long id,
		Long releaseId,
		String title,
		String description,
		ChecklistPhase phase,
		boolean required,
		boolean checked,
		String checkedBy,
		LocalDateTime checkedAt
) {

	public static ChecklistItemResponse from(ChecklistItem item) {
		return new ChecklistItemResponse(
				item.getId(),
				item.getRelease().getId(),
				item.getTitle(),
				item.getDescription(),
				item.getPhase(),
				item.isRequired(),
				item.isChecked(),
				item.getCheckedBy(),
				item.getCheckedAt()
		);
	}
}
