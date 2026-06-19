package com.deploypilot.domain.cicd;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CiRunRepository extends JpaRepository<CiRun, Long> {
}
