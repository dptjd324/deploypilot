package com.deploypilot.web.health;

import com.deploypilot.domain.health.HealthCheckRunner;
import com.deploypilot.domain.health.dto.HealthCheckResultResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/releases/{releaseId}")
public class HealthCheckController {

	private final HealthCheckRunner healthCheckRunner;

	public HealthCheckController(HealthCheckRunner healthCheckRunner) {
		this.healthCheckRunner = healthCheckRunner;
	}

	@PostMapping("/health-check/run")
	public HealthCheckResultResponse run(@PathVariable Long releaseId) {
		return healthCheckRunner.run(releaseId);
	}

	@GetMapping("/health-checks")
	public List<HealthCheckResultResponse> findByReleaseId(@PathVariable Long releaseId) {
		return healthCheckRunner.findByReleaseId(releaseId);
	}
}
