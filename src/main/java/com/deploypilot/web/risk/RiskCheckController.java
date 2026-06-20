package com.deploypilot.web.risk;

import com.deploypilot.domain.risk.RiskCheckService;
import com.deploypilot.domain.risk.dto.RiskCheckRequest;
import com.deploypilot.domain.risk.dto.RiskCheckResultResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/releases/{releaseId}")
public class RiskCheckController {

	private final RiskCheckService riskCheckService;

	public RiskCheckController(RiskCheckService riskCheckService) {
		this.riskCheckService = riskCheckService;
	}

	@PostMapping("/risk-check/db-migration")
	public RiskCheckResultResponse checkDbMigration(
			@PathVariable Long releaseId,
			@Valid @RequestBody RiskCheckRequest request
	) {
		return riskCheckService.checkDbMigration(releaseId, request);
	}

	@PostMapping("/risk-check/security-config")
	public RiskCheckResultResponse checkSecurityConfig(
			@PathVariable Long releaseId,
			@Valid @RequestBody RiskCheckRequest request
	) {
		return riskCheckService.checkSecurityConfig(releaseId, request);
	}

	@GetMapping("/risk-checks")
	public List<RiskCheckResultResponse> findByReleaseId(@PathVariable Long releaseId) {
		return riskCheckService.findByReleaseId(releaseId);
	}
}
