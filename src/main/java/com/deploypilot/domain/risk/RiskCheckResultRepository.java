package com.deploypilot.domain.risk;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiskCheckResultRepository extends JpaRepository<RiskCheckResult, Long> {

	List<RiskCheckResult> findByReleaseIdOrderByCheckedAtDesc(Long releaseId);
}
