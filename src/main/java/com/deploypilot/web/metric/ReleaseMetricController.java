package com.deploypilot.web.metric;

import com.deploypilot.domain.metric.ReleaseMetricService;
import com.deploypilot.domain.metric.dto.ReleaseMetricCreateRequest;
import com.deploypilot.domain.metric.dto.ReleaseMetricResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/releases/{releaseId}/metrics")
public class ReleaseMetricController {

	private final ReleaseMetricService releaseMetricService;

	public ReleaseMetricController(ReleaseMetricService releaseMetricService) {
		this.releaseMetricService = releaseMetricService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ReleaseMetricResponse collect(
			@PathVariable Long releaseId,
			@Valid @RequestBody ReleaseMetricCreateRequest request
	) {
		return releaseMetricService.collect(releaseId, request);
	}

	@GetMapping
	public List<ReleaseMetricResponse> findByReleaseId(@PathVariable Long releaseId) {
		return releaseMetricService.findByReleaseId(releaseId);
	}
}
