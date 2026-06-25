package com.deploypilot.domain.checklist;

import com.deploypilot.domain.checklist.dto.ChecklistItemCheckRequest;
import com.deploypilot.domain.checklist.dto.ChecklistItemCreateRequest;
import com.deploypilot.domain.checklist.dto.ChecklistItemResponse;
import com.deploypilot.domain.checklist.dto.ChecklistItemUpdateRequest;
import com.deploypilot.domain.release.Release;
import com.deploypilot.domain.release.ReleaseRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ChecklistService {

	private final ReleaseRepository releaseRepository;
	private final ChecklistItemRepository checklistItemRepository;

	public ChecklistService(ReleaseRepository releaseRepository, ChecklistItemRepository checklistItemRepository) {
		this.releaseRepository = releaseRepository;
		this.checklistItemRepository = checklistItemRepository;
	}

	@Transactional(readOnly = true)
	public List<ChecklistItemResponse> findByReleaseId(Long releaseId) {
		return checklistItemRepository.findByReleaseIdOrderByIdAsc(releaseId)
				.stream()
				.map(ChecklistItemResponse::from)
				.toList();
	}

	@Transactional
	public ChecklistItemResponse create(Long releaseId, ChecklistItemCreateRequest request) {
		Release release = getRelease(releaseId);
		ChecklistItem item = ChecklistItem.create(
				release,
				request.title(),
				request.description(),
				request.phase(),
				request.required()
		);
		return ChecklistItemResponse.from(checklistItemRepository.save(item));
	}

	@Transactional
	public ChecklistItemResponse update(Long releaseId, Long itemId, ChecklistItemUpdateRequest request) {
		ChecklistItem item = getChecklistItem(releaseId, itemId);
		item.update(request.title(), request.description(), request.phase(), request.required());
		return ChecklistItemResponse.from(item);
	}

	@Transactional
	public ChecklistItemResponse check(Long releaseId, Long itemId, ChecklistItemCheckRequest request) {
		ChecklistItem item = getChecklistItem(releaseId, itemId);
		item.check(request.checkedBy());
		return ChecklistItemResponse.from(item);
	}

	@Transactional
	public ChecklistItemResponse uncheck(Long releaseId, Long itemId) {
		ChecklistItem item = getChecklistItem(releaseId, itemId);
		item.uncheck();
		return ChecklistItemResponse.from(item);
	}

	@Transactional
	public void delete(Long releaseId, Long itemId) {
		ChecklistItem item = getChecklistItem(releaseId, itemId);
		checklistItemRepository.delete(item);
	}

	private Release getRelease(Long releaseId) {
		return releaseRepository.findById(releaseId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Release not found"));
	}

	private ChecklistItem getChecklistItem(Long releaseId, Long itemId) {
		return checklistItemRepository.findById(itemId)
				.filter(item -> item.getRelease().getId().equals(releaseId))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checklist item not found"));
	}
}
