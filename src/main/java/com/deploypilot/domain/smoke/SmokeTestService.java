package com.deploypilot.domain.smoke;

import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppRepository;
import com.deploypilot.domain.smoke.dto.SmokeTestCreateRequest;
import com.deploypilot.domain.smoke.dto.SmokeTestResponse;
import com.deploypilot.domain.smoke.dto.SmokeTestUpdateRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SmokeTestService {

	private final ServiceAppRepository serviceAppRepository;
	private final SmokeTestRepository smokeTestRepository;

	public SmokeTestService(ServiceAppRepository serviceAppRepository, SmokeTestRepository smokeTestRepository) {
		this.serviceAppRepository = serviceAppRepository;
		this.smokeTestRepository = smokeTestRepository;
	}

	@Transactional(readOnly = true)
	public List<SmokeTestResponse> findByServiceId(Long serviceId) {
		return smokeTestRepository.findByServiceAppIdOrderByIdAsc(serviceId)
				.stream()
				.map(SmokeTestResponse::from)
				.toList();
	}

	@Transactional
	public SmokeTestResponse create(Long serviceId, SmokeTestCreateRequest request) {
		ServiceApp serviceApp = getServiceApp(serviceId);
		SmokeTest smokeTest = SmokeTest.create(
				serviceApp,
				request.name(),
				request.method(),
				request.path(),
				request.expectedStatus(),
				request.maxResponseTimeMs(),
				request.required(),
				request.enabled()
		);
		return SmokeTestResponse.from(smokeTestRepository.save(smokeTest));
	}

	@Transactional
	public SmokeTestResponse update(Long serviceId, Long smokeTestId, SmokeTestUpdateRequest request) {
		getServiceApp(serviceId);
		SmokeTest smokeTest = smokeTestRepository.findById(smokeTestId)
				.filter(found -> found.getServiceApp().getId().equals(serviceId))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Smoke test not found"));
		smokeTest.update(
				request.name(),
				request.method(),
				request.path(),
				request.expectedStatus(),
				request.maxResponseTimeMs(),
				request.required(),
				request.enabled()
		);
		return SmokeTestResponse.from(smokeTest);
	}

	private ServiceApp getServiceApp(Long serviceId) {
		return serviceAppRepository.findById(serviceId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service app not found"));
	}
}
