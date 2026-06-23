package com.deploypilot.domain.metric;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deploypilot.domain.metric.dto.ReleaseMetricCreateRequest;
import com.deploypilot.domain.metric.dto.ReleaseMetricResponse;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ReleaseMetricServiceTest {

	@Mock
	private ReleaseRepository releaseRepository;

	@Mock
	private ReleaseMetricRepository releaseMetricRepository;

	@Test
	void collectStoresReleaseMetric() {
		ReleaseMetricService service = new ReleaseMetricService(releaseRepository, releaseMetricRepository);
		ReleaseMetricCreateRequest request = new ReleaseMetricCreateRequest(1000, 10, 120, 450);

		when(releaseRepository.findById(1L)).thenReturn(Optional.of(release()));
		when(releaseMetricRepository.save(any(ReleaseMetric.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ReleaseMetricResponse response = service.collect(1L, request);

		assertThat(response.totalRequests()).isEqualTo(1000);
		assertThat(response.error5xxCount()).isEqualTo(10);
		assertThat(response.errorRate()).isEqualByComparingTo(new BigDecimal("1.00"));
		assertThat(response.avgResponseTimeMs()).isEqualTo(120);
		assertThat(response.p95ResponseTimeMs()).isEqualTo(450);
	}

	@Test
	void collectRejectsErrorCountGreaterThanTotalRequests() {
		ReleaseMetricService service = new ReleaseMetricService(releaseRepository, releaseMetricRepository);
		ReleaseMetricCreateRequest request = new ReleaseMetricCreateRequest(10, 11, 120, 450);

		when(releaseRepository.findById(1L)).thenReturn(Optional.of(release()));

		assertThatThrownBy(() -> service.collect(1L, request))
				.isInstanceOf(ResponseStatusException.class)
				.extracting("statusCode")
				.isEqualTo(HttpStatus.BAD_REQUEST);
		verify(releaseMetricRepository, never()).save(any());
	}

	private Release release() {
		return Release.create(
				serviceApp(),
				"v1.0.0",
				ServiceAppEnvironment.PRODUCTION,
				"main",
				"abc123",
				LocalDateTime.of(2026, 6, 23, 10, 0)
		);
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
