package com.deploypilot.web.gate;

import com.deploypilot.domain.gate.QualityGateService;
import com.deploypilot.domain.gate.dto.QualityCheckResultResponse;
import com.deploypilot.domain.gate.dto.QualityGateResultResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/releases/{releaseId}")
public class QualityGateController {

	private final QualityGateService qualityGateService;

	public QualityGateController(QualityGateService qualityGateService) {
		this.qualityGateService = qualityGateService;
	}

	@PostMapping("/quality-gate/run")
	public QualityGateResultResponse run(@PathVariable Long releaseId) {
		return qualityGateService.run(releaseId);
	}

	@GetMapping("/quality-gate")
	public QualityGateResultResponse findLatestResult(@PathVariable Long releaseId) {
		return qualityGateService.findLatestResult(releaseId);
	}

	@GetMapping("/quality-checks")
	public List<QualityCheckResultResponse> findCheckResults(@PathVariable Long releaseId) {
		return qualityGateService.findCheckResults(releaseId);
	}
}
