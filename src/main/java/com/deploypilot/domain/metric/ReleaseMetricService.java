package com.deploypilot.domain.metric;

import com.deploypilot.domain.metric.dto.ReleaseMetricCreateRequest;
import com.deploypilot.domain.metric.dto.ReleaseMetricResponse;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReleaseMetricService {

	private final ReleaseRepository releaseRepository;
	private final ReleaseMetricRepository releaseMetricRepository;

	public ReleaseMetricService(ReleaseRepository releaseRepository, ReleaseMetricRepository releaseMetricRepository) {
		this.releaseRepository = releaseRepository;
		this.releaseMetricRepository = releaseMetricRepository;
	}

	@Transactional
	public ReleaseMetricResponse collect(Long releaseId, ReleaseMetricCreateRequest request) {
		Release release = releaseRepository.findById(releaseId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found"));
		if (request.error5xxCount() > request.totalRequests()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "5xx error count cannot exceed total requests");
		}

		ReleaseMetric metric = ReleaseMetric.create(
				release,
				request.totalRequests(),
				request.error5xxCount(),
				request.avgResponseTimeMs(),
				request.p95ResponseTimeMs()
		);
		return ReleaseMetricResponse.from(releaseMetricRepository.save(metric));
	}

	@Transactional(readOnly = true)
	public List<ReleaseMetricResponse> findByReleaseId(Long releaseId) {
		return releaseMetricRepository.findByReleaseIdOrderByCollectedAtDesc(releaseId)
				.stream()
				.map(ReleaseMetricResponse::from)
				.toList();
	}
}
