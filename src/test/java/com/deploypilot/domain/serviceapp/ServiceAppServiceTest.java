package com.deploypilot.domain.serviceapp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deploypilot.domain.serviceapp.dto.ServiceAppCreateRequest;
import com.deploypilot.domain.serviceapp.dto.ServiceAppResponse;
import com.deploypilot.domain.serviceapp.dto.ServiceAppUpdateRequest;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class ServiceAppServiceTest {

	@Mock
	private ServiceAppRepository serviceAppRepository;

	@Test
	void createSavesActiveServiceApp() {
		ServiceAppService service = new ServiceAppService(serviceAppRepository);
		ServiceAppCreateRequest request = createRequest("order-api");

		when(serviceAppRepository.existsByName("order-api")).thenReturn(false);
		when(serviceAppRepository.save(any(ServiceApp.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ServiceAppResponse response = service.create(request);

		ArgumentCaptor<ServiceApp> captor = ArgumentCaptor.forClass(ServiceApp.class);
		verify(serviceAppRepository).save(captor.capture());
		ServiceApp saved = captor.getValue();

		assertThat(response.name()).isEqualTo("order-api");
		assertThat(saved.getStatus()).isEqualTo(ServiceAppStatus.ACTIVE);
		assertThat(saved.getEnvironment()).isEqualTo(ServiceAppEnvironment.PRODUCTION);
	}

	@Test
	void createRejectsDuplicateName() {
		ServiceAppService service = new ServiceAppService(serviceAppRepository);
		ServiceAppCreateRequest request = createRequest("order-api");

		when(serviceAppRepository.existsByName("order-api")).thenReturn(true);

		assertThatThrownBy(() -> service.create(request))
				.isInstanceOf(ResponseStatusException.class)
				.extracting("statusCode")
				.isEqualTo(HttpStatus.CONFLICT);
		verify(serviceAppRepository, never()).save(any());
	}

	@Test
	void updateChangesServiceAppFields() {
		ServiceAppService service = new ServiceAppService(serviceAppRepository);
		ServiceApp serviceApp = ServiceApp.create(
				"order-api",
				"old",
				"https://github.com/example/order-api",
				"main",
				"platform",
				ServiceAppEnvironment.PRODUCTION,
				"https://order.example.com/health"
		);
		ServiceAppUpdateRequest request = new ServiceAppUpdateRequest(
				"order-api-v2",
				"new",
				"https://github.com/example/order-api-v2",
				"develop",
				"backend",
				ServiceAppEnvironment.STAGING,
				"https://staging-order.example.com/health",
				ServiceAppStatus.INACTIVE
		);

		when(serviceAppRepository.findById(1L)).thenReturn(Optional.of(serviceApp));
		when(serviceAppRepository.existsByNameAndIdNot("order-api-v2", 1L)).thenReturn(false);

		ServiceAppResponse response = service.update(1L, request);

		assertThat(response.name()).isEqualTo("order-api-v2");
		assertThat(response.description()).isEqualTo("new");
		assertThat(response.defaultBranch()).isEqualTo("develop");
		assertThat(response.teamName()).isEqualTo("backend");
		assertThat(response.environment()).isEqualTo(ServiceAppEnvironment.STAGING);
		assertThat(response.status()).isEqualTo(ServiceAppStatus.INACTIVE);
	}

	@Test
	void findByIdRejectsMissingServiceApp() {
		ServiceAppService service = new ServiceAppService(serviceAppRepository);

		when(serviceAppRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> service.findById(1L))
				.isInstanceOf(ResponseStatusException.class)
				.extracting("statusCode")
				.isEqualTo(HttpStatus.NOT_FOUND);
	}

	private ServiceAppCreateRequest createRequest(String name) {
		return new ServiceAppCreateRequest(
				name,
				"Order service",
				"https://github.com/example/order-api",
				"main",
				"platform",
				ServiceAppEnvironment.PRODUCTION,
				"https://order.example.com/health"
		);
	}
}
