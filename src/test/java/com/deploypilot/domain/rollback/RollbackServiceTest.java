package com.deploypilot.domain.rollback;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deploypilot.domain.audit.AuditAction;
import com.deploypilot.domain.audit.AuditLog;
import com.deploypilot.domain.audit.AuditLogRepository;
import com.deploypilot.domain.audit.AuditTargetType;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import com.deploypilot.domain.release.ReleaseStatus;
import com.deploypilot.domain.rollback.dto.RollbackRequest;
import com.deploypilot.domain.rollback.dto.RollbackResponse;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import com.deploypilot.domain.user.Role;
import com.deploypilot.domain.user.User;
import com.deploypilot.domain.user.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class RollbackServiceTest {

	@Mock
	private ReleaseRepository releaseRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private RollbackRecordRepository rollbackRecordRepository;

	@Mock
	private AuditLogRepository auditLogRepository;

	@Test
	void rollbackStoresRecordUpdatesReleaseAndCreatesAuditLog() {
		RollbackService service = service();
		Release release = release();
		User actor = user();
		RollbackRequest request = request();

		when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));
		when(userRepository.findById(1L)).thenReturn(Optional.of(actor));
		when(rollbackRecordRepository.save(any(RollbackRecord.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		RollbackResponse response = service.rollback(1L, request);

		assertThat(response.targetVersion()).isEqualTo("v0.9.0");
		assertThat(response.reason()).isEqualTo("High error rate after deploy");
		assertThat(response.createdByEmail()).isEqualTo("admin@example.com");
		assertThat(response.releaseStatus()).isEqualTo(ReleaseStatus.ROLLED_BACK);
		assertThat(release.getStatus()).isEqualTo(ReleaseStatus.ROLLED_BACK);
		assertThat(release.getRolledBackAt()).isNotNull();

		ArgumentCaptor<RollbackRecord> recordCaptor = ArgumentCaptor.forClass(RollbackRecord.class);
		verify(rollbackRecordRepository).save(recordCaptor.capture());
		RollbackRecord savedRecord = recordCaptor.getValue();
		assertThat(savedRecord.getRelease()).isSameAs(release);
		assertThat(savedRecord.getTargetVersion()).isEqualTo("v0.9.0");
		assertThat(savedRecord.getReason()).isEqualTo("High error rate after deploy");
		assertThat(savedRecord.getCreatedBy()).isSameAs(actor);

		ArgumentCaptor<AuditLog> auditCaptor = ArgumentCaptor.forClass(AuditLog.class);
		verify(auditLogRepository).save(auditCaptor.capture());
		AuditLog auditLog = auditCaptor.getValue();
		assertThat(auditLog.getActor()).isSameAs(actor);
		assertThat(auditLog.getAction()).isEqualTo(AuditAction.ROLLBACK_CREATED);
		assertThat(auditLog.getTargetType()).isEqualTo(AuditTargetType.RELEASE);
		assertThat(auditLog.getTargetId()).isEqualTo(release.getId());
		assertThat(auditLog.getMessage()).isEqualTo("Rollback to v0.9.0: High error rate after deploy");
		assertThat(auditLog.getIpAddress()).isEqualTo("127.0.0.1");
	}

	@Test
	void rollbackRejectsAlreadyRolledBackRelease() {
		RollbackService service = service();
		Release release = release();
		release.rollback();

		when(releaseRepository.findById(1L)).thenReturn(Optional.of(release));
		when(userRepository.findById(1L)).thenReturn(Optional.of(user()));

		assertThatThrownBy(() -> service.rollback(1L, request()))
				.isInstanceOf(ResponseStatusException.class)
				.extracting("statusCode")
				.isEqualTo(HttpStatus.CONFLICT);
		verify(rollbackRecordRepository, never()).save(any());
		verify(auditLogRepository, never()).save(any());
	}

	private RollbackService service() {
		return new RollbackService(releaseRepository, userRepository, rollbackRecordRepository, auditLogRepository);
	}

	private RollbackRequest request() {
		return new RollbackRequest(
				"v0.9.0",
				"High error rate after deploy",
				1L,
				"127.0.0.1"
		);
	}

	private Release release() {
		Release release = Release.create(
				serviceApp(),
				"v1.0.0",
				ServiceAppEnvironment.PRODUCTION,
				"main",
				"abc123",
				LocalDateTime.of(2026, 6, 23, 10, 0)
		);
		ReflectionTestUtils.setField(release, "id", 1L);
		return release;
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

	private User user() {
		User user = User.create("admin@example.com", "encoded-password", "Admin", Role.ADMIN);
		ReflectionTestUtils.setField(user, "id", 1L);
		return user;
	}
}
