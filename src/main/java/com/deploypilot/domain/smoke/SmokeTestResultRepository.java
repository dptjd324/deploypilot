package com.deploypilot.domain.smoke;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmokeTestResultRepository extends JpaRepository<SmokeTestResult, Long> {

	List<SmokeTestResult> findByReleaseIdOrderByExecutedAtDesc(Long releaseId);

	long countByReleaseId(Long releaseId);

	long countByReleaseIdAndStatus(Long releaseId, SmokeTestResultStatus status);
}
