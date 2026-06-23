package com.deploypilot.domain.dashboard;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deploypilot.domain.dashboard.dto.DoraDashboardResponse;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
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
		when(releaseRepository.findByDeployedAtBetweenAndCommitTimeIsNotNull(from, to)).thenReturn(List.of());

		DoraDashboardResponse response = service.calculate(from, to);

		assertThat(response.from()).isEqualTo(from);
		assertThat(response.to()).isEqualTo(to);
		assertThat(response.deploymentFrequency()).isEqualTo(12L);
		assertThat(response.averageLeadTimeMinutes()).isZero();
	}

	@Test
	void calculateAveragesLeadTimeForDeployedReleases() {
		DoraDashboardService service = new DoraDashboardService(releaseRepository);
		LocalDateTime from = LocalDateTime.of(2026, 6, 1, 0, 0);
		LocalDateTime to = LocalDateTime.of(2026, 6, 30, 23, 59);
		Release first = release(
				LocalDateTime.of(2026, 6, 10, 10, 0),
				LocalDateTime.of(2026, 6, 10, 11, 30)
		);
		Release second = release(
				LocalDateTime.of(2026, 6, 11, 9, 0),
				LocalDateTime.of(2026, 6, 11, 12, 0)
		);

		when(releaseRepository.countByDeployedAtBetween(from, to)).thenReturn(2L);
		when(releaseRepository.findByDeployedAtBetweenAndCommitTimeIsNotNull(from, to))
				.thenReturn(List.of(first, second));

		DoraDashboardResponse response = service.calculate(from, to);

		assertThat(response.deploymentFrequency()).isEqualTo(2L);
		assertThat(response.averageLeadTimeMinutes()).isEqualTo(135L);
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
		verify(releaseRepository, never()).findByDeployedAtBetweenAndCommitTimeIsNotNull(from, to);
	}

	private Release release(LocalDateTime commitTime, LocalDateTime deployedAt) {
		Release release = Release.create(
				serviceApp(),
				"v1.0.0",
				ServiceAppEnvironment.PRODUCTION,
				"main",
				"abc123",
				commitTime
		);
		ReflectionTestUtils.setField(release, "deployedAt", deployedAt);
		return release;
	}

	private ServiceApp serviceApp() {
		return ServiceApp.create(
				"order-api",
				"Order service",
				"https://github.com/example/order-api",
				"main",
				"platform",
				ServiceAppEnvironment.PRODUCTION,
				"https://order.example.com/health"
		);
	}
}
