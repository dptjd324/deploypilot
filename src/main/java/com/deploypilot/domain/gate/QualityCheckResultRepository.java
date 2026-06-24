package com.deploypilot.domain.gate;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualityCheckResultRepository extends JpaRepository<QualityCheckResult, Long> {

	List<QualityCheckResult> findByReleaseIdOrderByCheckedAtDesc(Long releaseId);
}
