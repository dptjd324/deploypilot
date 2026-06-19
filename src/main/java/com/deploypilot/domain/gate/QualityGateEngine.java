package com.deploypilot.domain.gate;

import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QualityGateEngine {

	private final List<QualityCheckEvaluator> evaluators;
	private final ReleaseRepository releaseRepository;
	private final QualityCheckResultRepository qualityCheckResultRepository;
	private final QualityGateResultRepository qualityGateResultRepository;

	public QualityGateEngine(
			List<QualityCheckEvaluator> evaluators,
			ReleaseRepository releaseRepository,
			QualityCheckResultRepository qualityCheckResultRepository,
			QualityGateResultRepository qualityGateResultRepository
	) {
		this.evaluators = evaluators;
		this.releaseRepository = releaseRepository;
		this.qualityCheckResultRepository = qualityCheckResultRepository;
		this.qualityGateResultRepository = qualityGateResultRepository;
	}

	@Transactional
	public QualityGateResult run(Long releaseId) {
		Release release = releaseRepository.findById(releaseId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found"));

		List<QualityCheckResult> checkResults = evaluators.stream()
				.map(evaluator -> evaluator.evaluate(release))
				.toList();

		qualityCheckResultRepository.saveAll(checkResults);

		GateResult gateResult = decide(checkResults);
		QualityGateResult qualityGateResult = QualityGateResult.create(
				release,
				gateResult,
				reason(gateResult)
		);
		release.applyGateResult(gateResult);
		return qualityGateResultRepository.save(qualityGateResult);
	}

	GateResult decide(List<QualityCheckResult> checkResults) {
		boolean hasBlockerFailure = checkResults.stream()
				.anyMatch(result -> result.getStatus() == CheckStatus.FAIL && result.getSeverity() == Severity.BLOCKER);
		if (hasBlockerFailure) {
			return GateResult.FAIL;
		}

		boolean hasWarning = checkResults.stream()
				.anyMatch(result -> result.getStatus() == CheckStatus.WARN
						|| result.getStatus() == CheckStatus.FAIL
						|| result.getSeverity() == Severity.WARNING);
		if (hasWarning) {
			return GateResult.WARN;
		}

		return GateResult.PASS;
	}

	private String reason(GateResult gateResult) {
		return switch (gateResult) {
			case FAIL -> "At least one blocker check failed";
			case WARN -> "No blocker failed, but warning checks exist";
			case PASS -> "All quality checks passed";
		};
	}
}
