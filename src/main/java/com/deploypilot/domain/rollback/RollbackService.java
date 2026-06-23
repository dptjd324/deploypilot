package com.deploypilot.domain.rollback;

import com.deploypilot.domain.audit.AuditLog;
import com.deploypilot.domain.audit.AuditLogRepository;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import com.deploypilot.domain.rollback.dto.RollbackRequest;
import com.deploypilot.domain.rollback.dto.RollbackResponse;
import com.deploypilot.domain.user.User;
import com.deploypilot.domain.user.UserRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RollbackService {

	private final ReleaseRepository releaseRepository;
	private final UserRepository userRepository;
	private final RollbackRecordRepository rollbackRecordRepository;
	private final AuditLogRepository auditLogRepository;

	public RollbackService(
			ReleaseRepository releaseRepository,
			UserRepository userRepository,
			RollbackRecordRepository rollbackRecordRepository,
			AuditLogRepository auditLogRepository
	) {
		this.releaseRepository = releaseRepository;
		this.userRepository = userRepository;
		this.rollbackRecordRepository = rollbackRecordRepository;
		this.auditLogRepository = auditLogRepository;
	}

	@Transactional
	public RollbackResponse rollback(Long releaseId, RollbackRequest request) {
		Release release = getRelease(releaseId);
		User actor = getUser(request.createdBy());

		transition(release::rollback);

		RollbackRecord record = RollbackRecord.create(
				release,
				request.targetVersion(),
				request.reason(),
				actor
		);
		RollbackRecord savedRecord = rollbackRecordRepository.save(record);

		auditLogRepository.save(AuditLog.createRollbackLog(
				actor,
				release.getId(),
				request.targetVersion(),
				request.reason(),
				request.ipAddress()
		));

		return RollbackResponse.from(savedRecord);
	}

	@Transactional(readOnly = true)
	public List<RollbackResponse> findByReleaseId(Long releaseId) {
		return rollbackRecordRepository.findByReleaseIdOrderByCreatedAtDesc(releaseId)
				.stream()
				.map(RollbackResponse::from)
				.toList();
	}

	private Release getRelease(Long releaseId) {
		return releaseRepository.findById(releaseId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found"));
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
	}

	private void transition(Runnable transition) {
		try {
			transition.run();
		} catch (IllegalStateException exception) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage(), exception);
		}
	}
}
