package com.deploypilot.web.serviceapp;

import com.deploypilot.domain.serviceapp.ServiceAppService;
import com.deploypilot.domain.serviceapp.dto.ServiceAppCreateRequest;
import com.deploypilot.domain.serviceapp.dto.ServiceAppResponse;
import com.deploypilot.domain.serviceapp.dto.ServiceAppUpdateRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services")
public class ServiceAppController {

	private final ServiceAppService serviceAppService;

	public ServiceAppController(ServiceAppService serviceAppService) {
		this.serviceAppService = serviceAppService;
	}

	@GetMapping
	public List<ServiceAppResponse> findAll() {
		return serviceAppService.findAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ServiceAppResponse create(@Valid @RequestBody ServiceAppCreateRequest request) {
		return serviceAppService.create(request);
	}

	@GetMapping("/{serviceId}")
	public ServiceAppResponse findById(@PathVariable Long serviceId) {
		return serviceAppService.findById(serviceId);
	}

	@PutMapping("/{serviceId}")
	public ServiceAppResponse update(
			@PathVariable Long serviceId,
			@Valid @RequestBody ServiceAppUpdateRequest request
	) {
		return serviceAppService.update(serviceId, request);
	}

	@DeleteMapping("/{serviceId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long serviceId) {
		serviceAppService.delete(serviceId);
	}
}
