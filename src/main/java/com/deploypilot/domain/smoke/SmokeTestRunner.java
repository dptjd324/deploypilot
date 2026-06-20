package com.deploypilot.domain.smoke;

import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.smoke.dto.SmokeTestResultResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SmokeTestRunner {

	private final ReleaseRepository releaseRepository;
	private final SmokeTestRepository smokeTestRepository;
	private final SmokeTestResultRepository smokeTestResultRepository;
	private final SmokeTestClient smokeTestClient;

	public SmokeTestRunner(
			ReleaseRepository releaseRepository,
			SmokeTestRepository smokeTestRepository,
			SmokeTestResultRepository smokeTestResultRepository,
			SmokeTestClient smokeTestClient
	) {
		this.releaseRepository = releaseRepository;
		this.smokeTestRepository = smokeTestRepository;
		this.smokeTestResultRepository = smokeTestResultRepository;
		this.smokeTestClient = smokeTestClient;
	}

	@Transactional
	public List<SmokeTestResultResponse> run(Long releaseId) {
		Release release = getRelease(releaseId);
		ServiceApp serviceApp = release.getServiceApp();
		List<SmokeTestResult> results = smokeTestRepository.findByServiceAppIdAndEnabledTrueOrderByIdAsc(serviceApp.getId())
				.stream()
				.map(smokeTest -> execute(release, serviceApp, smokeTest))
				.toList();

		return smokeTestResultRepository.saveAll(results)
				.stream()
				.map(SmokeTestResultResponse::from)
				.toList();
	}

	@Transactional(readOnly = true)
	public List<SmokeTestResultResponse> findByReleaseId(Long releaseId) {
		return smokeTestResultRepository.findByReleaseIdOrderByExecutedAtDesc(releaseId)
				.stream()
				.map(SmokeTestResultResponse::from)
				.toList();
	}

	private SmokeTestResult execute(Release release, ServiceApp serviceApp, SmokeTest smokeTest) {
		String url = buildSmokeTestUrl(serviceApp, smokeTest);
		try {
			SmokeTestHttpResponse response = smokeTestClient.execute(smokeTest.getMethod(), url);
			boolean passed = response.httpStatus() == smokeTest.getExpectedStatus()
					&& response.responseTimeMs() <= smokeTest.getMaxResponseTimeMs();
			return SmokeTestResult.create(
					release,
					smokeTest,
					passed ? SmokeTestResultStatus.PASS : SmokeTestResultStatus.FAIL,
					response.httpStatus(),
					response.responseTimeMs(),
					passed ? null : "Smoke test expectation was not met"
			);
		} catch (SmokeTestClientException exception) {
			return SmokeTestResult.create(
					release,
					smokeTest,
					SmokeTestResultStatus.FAIL,
					null,
					exception.getResponseTimeMs(),
					exception.getMessage()
			);
		}
	}

	private String buildSmokeTestUrl(ServiceApp serviceApp, SmokeTest smokeTest) {
		String healthCheckUrl = serviceApp.getHealthCheckUrl();
		if (healthCheckUrl == null || healthCheckUrl.isBlank()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Health check URL is required to build smoke test URL");
		}

		try {
			URI uri = new URI(healthCheckUrl);
			String path = smokeTest.getPath().startsWith("/") ? smokeTest.getPath() : "/" + smokeTest.getPath();
			return new URI(uri.getScheme(), uri.getAuthority(), path, null, null).toString();
		} catch (URISyntaxException exception) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid health check URL", exception);
		}
	}

	private Release getRelease(Long releaseId) {
		return releaseRepository.findById(releaseId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found"));
	}
}
