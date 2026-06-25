package com.deploypilot.web.checklist;

import com.deploypilot.domain.checklist.ChecklistService;
import com.deploypilot.domain.checklist.dto.ChecklistItemCheckRequest;
import com.deploypilot.domain.checklist.dto.ChecklistItemCreateRequest;
import com.deploypilot.domain.checklist.dto.ChecklistItemResponse;
import com.deploypilot.domain.checklist.dto.ChecklistItemUpdateRequest;
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
@RequestMapping("/api/releases/{releaseId}/checklist-items")
public class ChecklistController {

	private final ChecklistService checklistService;

	public ChecklistController(ChecklistService checklistService) {
		this.checklistService = checklistService;
	}

	@GetMapping
	public List<ChecklistItemResponse> findByReleaseId(@PathVariable Long releaseId) {
		return checklistService.findByReleaseId(releaseId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ChecklistItemResponse create(
			@PathVariable Long releaseId,
			@Valid @RequestBody ChecklistItemCreateRequest request
	) {
		return checklistService.create(releaseId, request);
	}

	@PutMapping("/{itemId}")
	public ChecklistItemResponse update(
			@PathVariable Long releaseId,
			@PathVariable Long itemId,
			@Valid @RequestBody ChecklistItemUpdateRequest request
	) {
		return checklistService.update(releaseId, itemId, request);
	}

	@PostMapping("/{itemId}/check")
	public ChecklistItemResponse check(
			@PathVariable Long releaseId,
			@PathVariable Long itemId,
			@Valid @RequestBody ChecklistItemCheckRequest request
	) {
		return checklistService.check(releaseId, itemId, request);
	}

	@PostMapping("/{itemId}/uncheck")
	public ChecklistItemResponse uncheck(@PathVariable Long releaseId, @PathVariable Long itemId) {
		return checklistService.uncheck(releaseId, itemId);
	}

	@DeleteMapping("/{itemId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long releaseId, @PathVariable Long itemId) {
		checklistService.delete(releaseId, itemId);
	}
}
