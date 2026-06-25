package com.deploypilot.domain.checklist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.deploypilot.domain.checklist.dto.ChecklistItemCheckRequest;
import com.deploypilot.domain.checklist.dto.ChecklistItemCreateRequest;
import com.deploypilot.domain.checklist.dto.ChecklistItemUpdateRequest;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import com.deploypilot.domain.serviceapp.ServiceApp;
import com.deploypilot.domain.serviceapp.ServiceAppEnvironment;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ChecklistServiceTest {

	@Mock
	private ReleaseRepository releaseRepository;

	@Mock
	private ChecklistItemRepository checklistItemRepository;

	@Test
	void createStoresChecklistItemForRelease() {
		ChecklistService service = new ChecklistService(releaseRepository, checklistItemRepository);
		ChecklistItemCreateRequest request = new ChecklistItemCreateRequest(
				"DB migration reviewed",
				"Review migration rollback plan",
				ChecklistPhase.PRE_DEPLOY,
				true
		);

		when(releaseRepository.findById(1L)).thenReturn(Optional.of(release()));
		when(checklistItemRepository.save(any(ChecklistItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

		var response = service.create(1L, request);

		assertThat(response.title()).isEqualTo("DB migration reviewed");
		assertThat(response.phase()).isEqualTo(ChecklistPhase.PRE_DEPLOY);
		assertThat(response.required()).isTrue();
		assertThat(response.checked()).isFalse();
	}

	@Test
	void checkMarksChecklistItemComplete() {
		ChecklistService service = new ChecklistService(releaseRepository, checklistItemRepository);
		ChecklistItem item = checklistItem();

		when(checklistItemRepository.findById(10L)).thenReturn(Optional.of(item));

		var response = service.check(1L, 10L, new ChecklistItemCheckRequest("admin@example.com"));

		assertThat(response.checked()).isTrue();
		assertThat(response.checkedBy()).isEqualTo("admin@example.com");
		assertThat(response.checkedAt()).isNotNull();
	}

	@Test
	void updateChangesEditableFields() {
		ChecklistService service = new ChecklistService(releaseRepository, checklistItemRepository);
		ChecklistItem item = checklistItem();
		ChecklistItemUpdateRequest request = new ChecklistItemUpdateRequest(
				"Smoke test prepared",
				"Confirm endpoint coverage",
				ChecklistPhase.POST_DEPLOY,
				false
		);

		when(checklistItemRepository.findById(10L)).thenReturn(Optional.of(item));

		var response = service.update(1L, 10L, request);

		assertThat(response.title()).isEqualTo("Smoke test prepared");
		assertThat(response.phase()).isEqualTo(ChecklistPhase.POST_DEPLOY);
		assertThat(response.required()).isFalse();
	}

	@Test
	void deleteRemovesChecklistItem() {
		ChecklistService service = new ChecklistService(releaseRepository, checklistItemRepository);
		ChecklistItem item = checklistItem();

		when(checklistItemRepository.findById(10L)).thenReturn(Optional.of(item));

		service.delete(1L, 10L);

		verify(checklistItemRepository).delete(item);
	}

	private ChecklistItem checklistItem() {
		ChecklistItem item = ChecklistItem.create(
				release(),
				"DB migration reviewed",
				"Review migration rollback plan",
				ChecklistPhase.PRE_DEPLOY,
				true
		);
		ReflectionTestUtils.setField(item, "id", 10L);
		return item;
	}

	private Release release() {
		Release release = Release.create(
				serviceApp(),
				"v1.0.0",
				ServiceAppEnvironment.PRODUCTION,
				"main",
				"abc123",
				LocalDateTime.of(2026, 6, 19, 10, 0)
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
}
