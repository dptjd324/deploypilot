package com.deploypilot.domain.smoke;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import com.deploypilot.domain.serviceapp.ServiceAppRepository;
import com.deploypilot.domain.smoke.dto.SmokeTestCreateRequest;
import com.deploypilot.domain.smoke.dto.SmokeTestResponse;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SmokeTestServiceTest {

	@Mock
	private ServiceAppRepository serviceAppRepository;

	@Mock
	private SmokeTestRepository smokeTestRepository;

	@Test
	void createStoresSmokeTestForService() {
		SmokeTestService service = new SmokeTestService(serviceAppRepository, smokeTestRepository);
		SmokeTestCreateRequest request = new SmokeTestCreateRequest(
				"Root endpoint",
				SmokeTestMethod.GET,
				"/",
				200,
				500,
				true,
				true
		);

		when(serviceAppRepository.findById(1L)).thenReturn(Optional.of(serviceApp()));
		when(smokeTestRepository.save(any(SmokeTest.class))).thenAnswer(invocation -> invocation.getArgument(0));

		SmokeTestResponse response = service.create(1L, request);

		assertThat(response.name()).isEqualTo("Root endpoint");
		assertThat(response.method()).isEqualTo(SmokeTestMethod.GET);
		assertThat(response.expectedStatus()).isEqualTo(200);
		assertThat(response.required()).isTrue();
		assertThat(response.enabled()).isTrue();
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
