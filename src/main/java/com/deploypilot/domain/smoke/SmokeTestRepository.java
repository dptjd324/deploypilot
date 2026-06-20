package com.deploypilot.domain.smoke;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmokeTestRepository extends JpaRepository<SmokeTest, Long> {

	List<SmokeTest> findByServiceAppIdOrderByIdAsc(Long serviceAppId);

	List<SmokeTest> findByServiceAppIdAndEnabledTrueOrderByIdAsc(Long serviceAppId);
}
