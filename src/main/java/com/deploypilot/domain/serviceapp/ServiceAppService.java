package com.deploypilot.domain.serviceapp;

import com.deploypilot.domain.serviceapp.dto.ServiceAppCreateRequest;
import com.deploypilot.domain.serviceapp.dto.ServiceAppResponse;
import com.deploypilot.domain.serviceapp.dto.ServiceAppUpdateRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ServiceAppService {

	private final ServiceAppRepository serviceAppRepository;

	public ServiceAppService(ServiceAppRepository serviceAppRepository) {
		this.serviceAppRepository = serviceAppRepository;
	}

	@Transactional
	public ServiceAppResponse create(ServiceAppCreateRequest request) {
		if (serviceAppRepository.existsByName(request.name())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Service app name already exists");
		}

		ServiceApp serviceApp = ServiceApp.create(
				request.name(),
				request.description(),
				request.repositoryUrl(),
				request.defaultBranch(),
				request.teamName(),
				request.environment(),
				request.healthCheckUrl()
		);

		return ServiceAppResponse.from(serviceAppRepository.save(serviceApp));
	}

	@Transactional(readOnly = true)
	public List<ServiceAppResponse> findAll() {
		return serviceAppRepository.findAll()
				.stream()
				.map(ServiceAppResponse::from)
				.toList();
	}

	@Transactional(readOnly = true)
	public ServiceAppResponse findById(Long serviceId) {
		return ServiceAppResponse.from(getServiceApp(serviceId));
	}

	@Transactional
	public ServiceAppResponse update(Long serviceId, ServiceAppUpdateRequest request) {
		ServiceApp serviceApp = getServiceApp(serviceId);
		if (serviceAppRepository.existsByNameAndIdNot(request.name(), serviceId)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Service app name already exists");
		}

		serviceApp.update(
				request.name(),
				request.description(),
				request.repositoryUrl(),
				request.defaultBranch(),
				request.teamName(),
				request.environment(),
				request.healthCheckUrl(),
				request.status()
		);

		return ServiceAppResponse.from(serviceApp);
	}

	@Transactional
	public void delete(Long serviceId) {
		ServiceApp serviceApp = getServiceApp(serviceId);
		serviceAppRepository.delete(serviceApp);
	}

	private ServiceApp getServiceApp(Long serviceId) {
		return serviceAppRepository.findById(serviceId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service app not found"));
	}
}
