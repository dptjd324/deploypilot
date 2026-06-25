package com.deploypilot.domain.checklist;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {

	long countByReleaseIdAndRequiredTrueAndCheckedFalse(Long releaseId);

	List<ChecklistItem> findByReleaseIdOrderByIdAsc(Long releaseId);
}
