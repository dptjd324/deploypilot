package com.deploypilot.domain.dashboard;

import com.deploypilot.domain.dashboard.dto.DoraDashboardResponse;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class DoraDashboardService {

	private final ReleaseRepository releaseRepository;

	public DoraDashboardService(ReleaseRepository releaseRepository) {
		this.releaseRepository = releaseRepository;
	}

	@Transactional(readOnly = true)
	public DoraDashboardResponse calculate(LocalDateTime from, LocalDateTime to) {
		if (from.isAfter(to)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "from must be before or equal to to");
		}

		long deploymentFrequency = releaseRepository.countByDeployedAtBetween(from, to);
		long averageLeadTimeMinutes = calculateAverageLeadTimeMinutes(from, to);
		return new DoraDashboardResponse(from, to, deploymentFrequency, averageLeadTimeMinutes);
	}

	private long calculateAverageLeadTimeMinutes(LocalDateTime from, LocalDateTime to) {
		List<Release> deployedReleases = releaseRepository.findByDeployedAtBetweenAndCommitTimeIsNotNull(from, to);
		if (deployedReleases.isEmpty()) {
			return 0;
		}

		return Math.round(deployedReleases.stream()
				.mapToLong(release -> Duration.between(release.getCommitTime(), release.getDeployedAt()).toMinutes())
				.average()
				.orElse(0));
	}
}
