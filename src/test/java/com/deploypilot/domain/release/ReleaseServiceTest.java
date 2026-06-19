package com.deploypilot.domain.release;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.deploypilot.domain.release.dto.ReleaseCreateRequest;
import com.deploypilot.domain.release.dto.ReleaseResponse;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import com.deploypilot.domain.serviceapp.ServiceAppRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ReleaseServiceTest {

	@Mock
	private ReleaseRepository releaseRepository;

	@Mock
	private ServiceAppRepository serviceAppRepository;

	@Test
	void createSavesReleaseWithInitialStatuses() {
		ReleaseService service = new ReleaseService(releaseRepository, serviceAppRepository);
		ServiceApp serviceApp = serviceApp();
		ReleaseCreateRequest request = createRequest();

		when(serviceAppRepository.findById(1L)).thenReturn(Optional.of(serviceApp));
		when(releaseRepository.save(any(Release.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ReleaseResponse response = service.create(request);

		assertThat(response.serviceName()).isEqualTo("order-api");
		assertThat(response.version()).isEqualTo("v1.0.0");
		assertThat(response.status()).isEqualTo(ReleaseStatus.CREATED);
		assertThat(response.gateStatus()).isEqualTo(GateStatus.PENDING);
	}

	@Test
	void createRejectsMissingServiceApp() {
		ReleaseService service = new ReleaseService(releaseRepository, serviceAppRepository);

		when(serviceAppRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.create(createRequest()))
				.isInstanceOf(ResponseStatusException.class)
				.extracting("statusCode")
				.isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void approveChangesCreatedReleaseToApproved() {
		ReleaseService service = new ReleaseService(releaseRepository, serviceAppRepository);
		Release release = release();

		when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));

		ReleaseResponse response = service.approve(1L);

		assertThat(response.status()).isEqualTo(ReleaseStatus.APPROVED);
	}

	@Test
	void deployRequiresApprovedRelease() {
		ReleaseService service = new ReleaseService(releaseRepository, serviceAppRepository);
		Release release = release();

		when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));

		assertThatThrownBy(() -> service.deploy(1L))
				.isInstanceOf(ResponseStatusException.class)
				.extracting("statusCode")
				.isEqualTo(HttpStatus.CONFLICT);
	}

	@Test
	void deployChangesApprovedReleaseToDeployed() {
		ReleaseService service = new ReleaseService(releaseRepository, serviceAppRepository);
		Release release = release();
		release.approve();

		when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));

		ReleaseResponse response = service.deploy(1L);

		assertThat(response.status()).isEqualTo(ReleaseStatus.DEPLOYED);
		assertThat(response.deployedAt()).isNotNull();
	}

	@Test
	void rollbackChangesReleaseToRolledBack() {
		ReleaseService service = new ReleaseService(releaseRepository, serviceAppRepository);
		Release release = release();

		when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));

		ReleaseResponse response = service.rollback(1L);

		assertThat(response.status()).isEqualTo(ReleaseStatus.ROLLED_BACK);
		assertThat(response.rolledBackAt()).isNotNull();
	}

	private ReleaseCreateRequest createRequest() {
		return new ReleaseCreateRequest(
				1L,
				"v1.0.0",
				ServiceAppEnvironment.PRODUCTION,
				"main",
				"abc123",
				LocalDateTime.of(2026, 6, 19, 10, 0)
		);
	}

	private Release release() {
		return Release.create(
				serviceApp(),
				"v1.0.0",
				ServiceAppEnvironment.PRODUCTION,
				"main",
				"abc123",
				LocalDateTime.of(2026, 6, 19, 10, 0)
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
