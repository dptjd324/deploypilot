package com.deploypilot.domain.risk;

import com.deploypilot.domain.gate.CheckStatus;
import com.deploypilot.domain.gate.Severity;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class DbMigrationRiskChecker {

	private static final List<String> BLOCKER_PATTERNS = List.of(
			"DROP TABLE",
			"DROP COLUMN",
			"TRUNCATE TABLE",
			"DELETE FROM"
	);

	private static final List<String> WARNING_PATTERNS = List.of(
			"ALTER TABLE",
			"CREATE INDEX CONCURRENTLY",
			"UPDATE "
	);

	public RiskCheckDecision check(String targetText) {
		String normalized = normalize(targetText);

		return BLOCKER_PATTERNS.stream()
				.filter(normalized::contains)
				.findFirst()
				.map(pattern -> new RiskCheckDecision(
						Severity.BLOCKER,
						CheckStatus.FAIL,
						"DB migration contains destructive operation",
						pattern
				))
				.orElseGet(() -> WARNING_PATTERNS.stream()
						.filter(normalized::contains)
						.findFirst()
						.map(pattern -> new RiskCheckDecision(
								Severity.WARNING,
								CheckStatus.WARN,
								"DB migration contains operation that needs review",
								pattern
						))
						.orElseGet(() -> new RiskCheckDecision(
								Severity.INFO,
								CheckStatus.PASS,
								"DB migration risk check passed",
								null
						)));
	}

	private String normalize(String targetText) {
		return targetText == null ? "" : targetText.toUpperCase(Locale.ROOT);
	}
}
