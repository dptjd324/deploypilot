package com.deploypilot.domain.gate;

import com.deploypilot.domain.checklist.ChecklistItemRepository;
import com.deploypilot.domain.release.Release;
import org.springframework.stereotype.Component;

@Component
public class ChecklistEvaluator implements QualityCheckEvaluator {

	private final ChecklistItemRepository checklistItemRepository;

	public ChecklistEvaluator(ChecklistItemRepository checklistItemRepository) {
		this.checklistItemRepository = checklistItemRepository;
	}

	@Override
	public QualityCheckResult evaluate(Release release) {
		long uncheckedRequiredCount = checklistItemRepository.countByReleaseIdAndRequiredTrueAndCheckedFalse(release.getId());
		if (uncheckedRequiredCount == 0) {
			return QualityCheckResult.create(
					release,
					CheckType.CHECKLIST,
					CheckStatus.PASS,
					Severity.INFO,
					"Required checklist items are complete",
					null
			);
		}

		return QualityCheckResult.create(
				release,
				CheckType.CHECKLIST,
				CheckStatus.WARN,
				Severity.WARNING,
				"Required checklist items are not complete",
				"{\"uncheckedRequiredCount\":" + uncheckedRequiredCount + "}"
		);
	}
}
