package com.deploypilot.domain.gate;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualityGateResultRepository extends JpaRepository<QualityGateResult, Long> {

	Optional<QualityGateResult> findTopByReleaseIdOrderByCheckedAtDesc(Long releaseId);
}
