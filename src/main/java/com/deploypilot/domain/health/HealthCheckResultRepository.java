package com.deploypilot.domain.health;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthCheckResultRepository extends JpaRepository<HealthCheckResult, Long> {

	List<HealthCheckResult> findByReleaseIdOrderByCheckedAtDesc(Long releaseId);

	Optional<HealthCheckResult> findTopByReleaseIdOrderByCheckedAtDesc(Long releaseId);
}
