package com.deploypilot.web.cicd;

import com.deploypilot.domain.cicd.CiRunService;
import com.deploypilot.domain.cicd.dto.CiRunResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CiRunController {

	private final CiRunService ciRunService;

	public CiRunController(CiRunService ciRunService) {
		this.ciRunService = ciRunService;
	}

	@GetMapping("/ci-runs")
	public List<CiRunResponse> findRecentRuns() {
		return ciRunService.findRecentRuns();
	}

	@GetMapping("/releases/{releaseId}/ci-runs")
	public List<CiRunResponse> findByReleaseId(@PathVariable Long releaseId) {
		return ciRunService.findByReleaseId(releaseId);
	}
}
