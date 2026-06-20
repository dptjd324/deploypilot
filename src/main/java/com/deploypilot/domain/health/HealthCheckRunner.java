package com.deploypilot.domain.health;

import com.deploypilot.domain.health.dto.HealthCheckResultResponse;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import com.deploypilot.domain.serviceapp.ServiceApp;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HealthCheckRunner {

	private final ReleaseRepository releaseRepository;
	private final HealthCheckResultRepository healthCheckResultRepository;
	private final HealthCheckClient healthCheckClient;

	public HealthCheckRunner(
			ReleaseRepository releaseRepository,
			HealthCheckResultRepository healthCheckResultRepository,
			HealthCheckClient healthCheckClient
	) {
		this.releaseRepository = releaseRepository;
		this.healthCheckResultRepository = healthCheckResultRepository;
		this.healthCheckClient = healthCheckClient;
	}

	@Transactional
	public HealthCheckResultResponse run(Long releaseId) {
		Release release = getRelease(releaseId);
		ServiceApp serviceApp = release.getServiceApp();
		String healthCheckUrl = serviceApp.getHealthCheckUrl();
		if (healthCheckUrl == null || healthCheckUrl.isBlank()) {
			HealthCheckResult result = HealthCheckResult.failure(
					release,
					serviceApp,
					null,
					null,
					null,
					"Health check URL is not configured"
			);
			return HealthCheckResultResponse.from(healthCheckResultRepository.save(result));
		}

		try {
			HealthCheckResponse response = healthCheckClient.get(healthCheckUrl);
			HealthCheckResult result = toResult(release, serviceApp, response);
			return HealthCheckResultResponse.from(healthCheckResultRepository.save(result));
		} catch (HealthCheckClientException exception) {
			HealthCheckResult result = HealthCheckResult.failure(
					release,
					serviceApp,
					null,
					exception.getResponseTimeMs(),
					null,
					exception.getMessage()
			);
			return HealthCheckResultResponse.from(healthCheckResultRepository.save(result));
		}
	}

	@Transactional(readOnly = true)
	public List<HealthCheckResultResponse> findByReleaseId(Long releaseId) {
		return healthCheckResultRepository.findByReleaseIdOrderByCheckedAtDesc(releaseId)
				.stream()
				.map(HealthCheckResultResponse::from)
				.toList();
	}

	private HealthCheckResult toResult(Release release, ServiceApp serviceApp, HealthCheckResponse response) {
		if (response.httpStatus() >= 200 && response.httpStatus() < 300) {
			return HealthCheckResult.success(
					release,
					serviceApp,
					response.httpStatus(),
					response.responseTimeMs(),
					response.responseBody()
			);
		}
		return HealthCheckResult.failure(
				release,
				serviceApp,
				response.httpStatus(),
				response.responseTimeMs(),
				response.responseBody(),
				"Health check returned non-2xx status"
		);
	}

	private Release getRelease(Long releaseId) {
		return releaseRepository.findById(releaseId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found"));
	}
}
