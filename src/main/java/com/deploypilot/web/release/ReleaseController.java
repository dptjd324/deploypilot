package com.deploypilot.web.release;

import com.deploypilot.domain.release.ReleaseService;
import com.deploypilot.domain.release.dto.ReleaseCreateRequest;
import com.deploypilot.domain.release.dto.ReleaseResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/releases")
public class ReleaseController {

	private final ReleaseService releaseService;

	public ReleaseController(ReleaseService releaseService) {
		this.releaseService = releaseService;
	}

	@GetMapping
	public List<ReleaseResponse> findAll() {
		return releaseService.findAll();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ReleaseResponse create(@Valid @RequestBody ReleaseCreateRequest request) {
		return releaseService.create(request);
	}

	@GetMapping("/{releaseId}")
	public ReleaseResponse findById(@PathVariable Long releaseId) {
		return releaseService.findById(releaseId);
	}

	@PostMapping("/{releaseId}/approve")
	public ReleaseResponse approve(@PathVariable Long releaseId) {
		return releaseService.approve(releaseId);
	}

	@PostMapping("/{releaseId}/deploy")
	public ReleaseResponse deploy(@PathVariable Long releaseId) {
		return releaseService.deploy(releaseId);
	}

	@PostMapping("/{releaseId}/rollback")
	public ReleaseResponse rollback(@PathVariable Long releaseId) {
		return releaseService.rollback(releaseId);
	}
}
