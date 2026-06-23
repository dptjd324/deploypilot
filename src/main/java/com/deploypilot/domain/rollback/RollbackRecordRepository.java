package com.deploypilot.domain.rollback;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RollbackRecordRepository extends JpaRepository<RollbackRecord, Long> {

	List<RollbackRecord> findByReleaseIdOrderByCreatedAtDesc(Long releaseId);
}
