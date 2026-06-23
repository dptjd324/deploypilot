package com.deploypilot.domain.dashboard;

import com.deploypilot.domain.dashboard.dto.DoraDashboardResponse;
import com.deploypilot.domain.release.ReleaseRepository;
import java.time.LocalDateTime;
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
		return new DoraDashboardResponse(from, to, deploymentFrequency);
	}
}
