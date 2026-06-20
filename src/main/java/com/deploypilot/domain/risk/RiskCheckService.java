package com.deploypilot.domain.risk;

import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import com.deploypilot.domain.risk.dto.RiskCheckRequest;
import com.deploypilot.domain.risk.dto.RiskCheckResultResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RiskCheckService {

	private final ReleaseRepository releaseRepository;
	private final RiskCheckResultRepository riskCheckResultRepository;
	private final DbMigrationRiskChecker dbMigrationRiskChecker;
	private final SecurityConfigRiskChecker securityConfigRiskChecker;

	public RiskCheckService(
			ReleaseRepository releaseRepository,
			RiskCheckResultRepository riskCheckResultRepository,
			DbMigrationRiskChecker dbMigrationRiskChecker,
			SecurityConfigRiskChecker securityConfigRiskChecker
	) {
		this.releaseRepository = releaseRepository;
		this.riskCheckResultRepository = riskCheckResultRepository;
		this.dbMigrationRiskChecker = dbMigrationRiskChecker;
		this.securityConfigRiskChecker = securityConfigRiskChecker;
	}

	@Transactional
	public RiskCheckResultResponse checkDbMigration(Long releaseId, RiskCheckRequest request) {
		return runCheck(releaseId, RiskType.DB_MIGRATION, dbMigrationRiskChecker.check(request.targetText()), request.targetText());
	}

	@Transactional
	public RiskCheckResultResponse checkSecurityConfig(Long releaseId, RiskCheckRequest request) {
		return runCheck(releaseId, RiskType.SECURITY_CONFIG, securityConfigRiskChecker.check(request.targetText()), request.targetText());
	}

	@Transactional(readOnly = true)
	public List<RiskCheckResultResponse> findByReleaseId(Long releaseId) {
		return riskCheckResultRepository.findByReleaseIdOrderByCheckedAtDesc(releaseId)
				.stream()
				.map(RiskCheckResultResponse::from)
				.toList();
	}

	private RiskCheckResultResponse runCheck(
			Long releaseId,
			RiskType riskType,
			RiskCheckDecision decision,
			String targetText
	) {
		Release release = releaseRepository.findById(releaseId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found"));
		RiskCheckResult result = RiskCheckResult.create(
				release,
				riskType,
				decision.severity(),
				decision.status(),
				decision.message(),
				decision.detectedPattern(),
				targetText
		);
		return RiskCheckResultResponse.from(riskCheckResultRepository.save(result));
	}
}
