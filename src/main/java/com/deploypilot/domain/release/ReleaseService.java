package com.deploypilot.domain.release;

import com.deploypilot.domain.release.dto.ReleaseCreateRequest;
import com.deploypilot.domain.release.dto.ReleaseResponse;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReleaseService {

	private final ReleaseRepository releaseRepository;
	private final ServiceAppRepository serviceAppRepository;

	public ReleaseService(ReleaseRepository releaseRepository, ServiceAppRepository serviceAppRepository) {
		this.releaseRepository = releaseRepository;
		this.serviceAppRepository = serviceAppRepository;
	}

	@Transactional
	public ReleaseResponse create(ReleaseCreateRequest request) {
		ServiceApp serviceApp = getServiceApp(request.serviceAppId());
		Release release = Release.create(
				serviceApp,
				request.version(),
				request.environment(),
				request.branch(),
				request.commitHash(),
				request.commitTime()
		);

		return ReleaseResponse.from(releaseRepository.save(release));
	}

	@Transactional(readOnly = true)
	public List<ReleaseResponse> findAll() {
		return releaseRepository.findAll()
				.stream()
				.map(ReleaseResponse::from)
				.toList();
	}

	@Transactional(readOnly = true)
	public ReleaseResponse findById(Long releaseId) {
		return ReleaseResponse.from(getRelease(releaseId));
	}

	@Transactional
	public ReleaseResponse approve(Long releaseId) {
		Release release = getRelease(releaseId);
		transition(release::approve);
		return ReleaseResponse.from(release);
	}

	@Transactional
	public ReleaseResponse deploy(Long releaseId) {
		Release release = getRelease(releaseId);
		transition(release::deploy);
		return ReleaseResponse.from(release);
	}

	@Transactional
	public ReleaseResponse rollback(Long releaseId) {
		Release release = getRelease(releaseId);
		transition(release::rollback);
		return ReleaseResponse.from(release);
	}

	private ServiceApp getServiceApp(Long serviceAppId) {
		return serviceAppRepository.findById(serviceAppId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service app not found"));
	}

	private Release getRelease(Long releaseId) {
		return releaseRepository.findById(releaseId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found"));
	}

	private void transition(Runnable transition) {
		try {
			transition.run();
		} catch (IllegalStateException exception) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage(), exception);
		}
	}
}
