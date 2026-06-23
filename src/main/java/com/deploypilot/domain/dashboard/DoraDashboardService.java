package com.deploypilot.domain.dashboard;

import com.deploypilot.domain.dashboard.dto.DoraDashboardResponse;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import com.deploypilot.domain.release.ReleaseStatus;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
		BigDecimal changeFailureRate = calculateChangeFailureRate(from, to, deploymentFrequency);
		return new DoraDashboardResponse(from, to, deploymentFrequency, averageLeadTimeMinutes, changeFailureRate);
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

	private BigDecimal calculateChangeFailureRate(LocalDateTime from, LocalDateTime to, long deploymentFrequency) {
		if (deploymentFrequency == 0) {
			return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
		}

		long failedDeployments = releaseRepository.countByDeployedAtBetweenAndStatusIn(
				from,
				to,
				List.of(ReleaseStatus.FAILED, ReleaseStatus.ROLLED_BACK)
		);

		return BigDecimal.valueOf(failedDeployments)
				.multiply(BigDecimal.valueOf(100))
				.divide(BigDecimal.valueOf(deploymentFrequency), 2, RoundingMode.HALF_UP);
	}
}
