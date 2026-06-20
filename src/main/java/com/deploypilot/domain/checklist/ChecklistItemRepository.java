package com.deploypilot.domain.checklist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {

	long countByReleaseIdAndRequiredTrueAndCheckedFalse(Long releaseId);
}
