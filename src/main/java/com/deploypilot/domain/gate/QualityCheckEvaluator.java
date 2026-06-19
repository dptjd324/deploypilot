package com.deploypilot.domain.gate;

import com.deploypilot.domain.release.Release;

public interface QualityCheckEvaluator {

	QualityCheckResult evaluate(Release release);
}
