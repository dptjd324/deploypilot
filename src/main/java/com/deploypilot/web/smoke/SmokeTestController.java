package com.deploypilot.web.smoke;

import com.deploypilot.domain.smoke.SmokeTestRunner;
import com.deploypilot.domain.smoke.SmokeTestService;
import com.deploypilot.domain.smoke.dto.SmokeTestCreateRequest;
import com.deploypilot.domain.smoke.dto.SmokeTestResponse;
import com.deploypilot.domain.smoke.dto.SmokeTestResultResponse;
import com.deploypilot.domain.smoke.dto.SmokeTestUpdateRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmokeTestController {

	private final SmokeTestService smokeTestService;
	private final SmokeTestRunner smokeTestRunner;

	public SmokeTestController(SmokeTestService smokeTestService, SmokeTestRunner smokeTestRunner) {
		this.smokeTestService = smokeTestService;
		this.smokeTestRunner = smokeTestRunner;
	}

	@GetMapping("/api/services/{serviceId}/smoke-tests")
	public List<SmokeTestResponse> findByServiceId(@PathVariable Long serviceId) {
		return smokeTestService.findByServiceId(serviceId);
	}

	@PostMapping("/api/services/{serviceId}/smoke-tests")
	@ResponseStatus(HttpStatus.CREATED)
	public SmokeTestResponse create(
			@PathVariable Long serviceId,
			@Valid @RequestBody SmokeTestCreateRequest request
	) {
		return smokeTestService.create(serviceId, request);
	}

	@PutMapping("/api/services/{serviceId}/smoke-tests/{smokeTestId}")
	public SmokeTestResponse update(
			@PathVariable Long serviceId,
			@PathVariable Long smokeTestId,
			@Valid @RequestBody SmokeTestUpdateRequest request
	) {
		return smokeTestService.update(serviceId, smokeTestId, request);
	}

	@PostMapping("/api/releases/{releaseId}/smoke-tests/run")
	public List<SmokeTestResultResponse> run(@PathVariable Long releaseId) {
		return smokeTestRunner.run(releaseId);
	}

	@GetMapping("/api/releases/{releaseId}/smoke-test-results")
	public List<SmokeTestResultResponse> findResults(@PathVariable Long releaseId) {
		return smokeTestRunner.findByReleaseId(releaseId);
	}
}
