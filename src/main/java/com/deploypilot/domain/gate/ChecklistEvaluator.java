package com.deploypilot.domain.gate;

import com.deploypilot.domain.release.Release;
import org.springframework.stereotype.Component;

@Component
public class ChecklistEvaluator implements QualityCheckEvaluator {

	@Override
	public QualityCheckResult evaluate(Release release) {
		return QualityCheckResult.create(
				release,
				CheckType.CHECKLIST,
				CheckStatus.WARN,
				Severity.WARNING,
				"Checklist module is not configured yet",
				null
		);
	}
}
