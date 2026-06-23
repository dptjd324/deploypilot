package com.deploypilot.domain.dashboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deploypilot.domain.dashboard.dto.DoraDashboardResponse;
import com.deploypilot.domain.release.ReleaseRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class DoraDashboardServiceTest {

	@Mock
	private ReleaseRepository releaseRepository;

	@Test
	void calculateCountsDeploymentsInPeriod() {
		DoraDashboardService service = new DoraDashboardService(releaseRepository);
		LocalDateTime from = LocalDateTime.of(2026, 6, 1, 0, 0);
		LocalDateTime to = LocalDateTime.of(2026, 6, 30, 23, 59);

		when(releaseRepository.countByDeployedAtBetween(from, to)).thenReturn(12L);

		DoraDashboardResponse response = service.calculate(from, to);

		assertThat(response.from()).isEqualTo(from);
		assertThat(response.to()).isEqualTo(to);
		assertThat(response.deploymentFrequency()).isEqualTo(12L);
	}

	@Test
	void calculateRejectsInvalidPeriod() {
		DoraDashboardService service = new DoraDashboardService(releaseRepository);
		LocalDateTime from = LocalDateTime.of(2026, 7, 1, 0, 0);
		LocalDateTime to = LocalDateTime.of(2026, 6, 30, 23, 59);

		assertThatThrownBy(() -> service.calculate(from, to))
				.isInstanceOf(ResponseStatusException.class)
				.extracting("statusCode")
				.isEqualTo(HttpStatus.BAD_REQUEST);
		verify(releaseRepository, never()).countByDeployedAtBetween(from, to);
	}
}
