package com.deploypilot.domain.cicd;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CiRunRepository extends JpaRepository<CiRun, Long> {

	Optional<CiRun> findTopByReleaseIdOrderByCreatedAtDesc(Long releaseId);
}
