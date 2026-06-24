package com.deploypilot.domain.gate;

import com.deploypilot.domain.gate.dto.QualityCheckResultResponse;
import com.deploypilot.domain.gate.dto.QualityGateResultResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class QualityGateService {

	private final QualityGateEngine qualityGateEngine;
	private final QualityGateResultRepository qualityGateResultRepository;
	private final QualityCheckResultRepository qualityCheckResultRepository;

	public QualityGateService(
			QualityGateEngine qualityGateEngine,
			QualityGateResultRepository qualityGateResultRepository,
			QualityCheckResultRepository qualityCheckResultRepository
	) {
		this.qualityGateEngine = qualityGateEngine;
		this.qualityGateResultRepository = qualityGateResultRepository;
		this.qualityCheckResultRepository = qualityCheckResultRepository;
	}

	@Transactional
	public QualityGateResultResponse run(Long releaseId) {
		return QualityGateResultResponse.from(qualityGateEngine.run(releaseId));
	}

	@Transactional(readOnly = true)
	public QualityGateResultResponse findLatestResult(Long releaseId) {
		return qualityGateResultRepository.findTopByReleaseIdOrderByCheckedAtDesc(releaseId)
				.map(QualityGateResultResponse::from)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quality gate result not found"));
	}

	@Transactional(readOnly = true)
	public List<QualityCheckResultResponse> findCheckResults(Long releaseId) {
		return qualityCheckResultRepository.findByReleaseIdOrderByCheckedAtDesc(releaseId)
				.stream()
				.map(QualityCheckResultResponse::from)
				.toList();
	}
}
