package com.deploypilot.domain.metric;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReleaseMetricRepository extends JpaRepository<ReleaseMetric, Long> {

	List<ReleaseMetric> findByReleaseIdOrderByCollectedAtDesc(Long releaseId);

	Optional<ReleaseMetric> findTopByReleaseIdOrderByCollectedAtDesc(Long releaseId);
}
