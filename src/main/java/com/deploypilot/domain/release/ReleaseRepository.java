package com.deploypilot.domain.release;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReleaseRepository extends JpaRepository<Release, Long> {

	Optional<Release> findByServiceApp_NameAndVersionAndBranchAndCommitHash(
			String serviceName,
			String version,
			String branch,
			String commitHash
	);

	long countByDeployedAtBetween(LocalDateTime from, LocalDateTime to);

	long countByDeployedAtBetweenAndStatusIn(LocalDateTime from, LocalDateTime to, List<ReleaseStatus> statuses);

	List<Release> findByDeployedAtBetweenAndCommitTimeIsNotNull(LocalDateTime from, LocalDateTime to);

	@Query("""
			select r
			from Release r
			where r.failedAt between :from and :to
				and (r.rolledBackAt is not null or r.stableAt is not null)
			""")
	List<Release> findRecoveredFailedDeployments(
			@Param("from") LocalDateTime from,
			@Param("to") LocalDateTime to
	);
}
