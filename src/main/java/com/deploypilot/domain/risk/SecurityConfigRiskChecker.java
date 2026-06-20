package com.deploypilot.domain.risk;

import com.deploypilot.domain.gate.CheckStatus;
import com.deploypilot.domain.gate.Severity;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfigRiskChecker {

	private static final List<String> BLOCKER_PATTERNS = List.of(
			"CSRF.DISABLE",
			"PERMITALL",
			"PASSWORD=",
			"SECRET=",
			"TOKEN="
	);

	private static final List<String> WARNING_PATTERNS = List.of(
			"ANONYMOUS",
			"REMEMBERME",
			"BASIC"
	);

	public RiskCheckDecision check(String targetText) {
		String normalized = normalize(targetText);

		return BLOCKER_PATTERNS.stream()
				.filter(pattern -> normalized.replace(" ", "").contains(pattern))
				.findFirst()
				.map(pattern -> new RiskCheckDecision(
						Severity.BLOCKER,
						CheckStatus.FAIL,
						"Security config contains high risk pattern",
						pattern
				))
				.orElseGet(() -> WARNING_PATTERNS.stream()
						.filter(normalized::contains)
						.findFirst()
						.map(pattern -> new RiskCheckDecision(
								Severity.WARNING,
								CheckStatus.WARN,
								"Security config contains setting that needs review",
								pattern
						))
						.orElseGet(() -> new RiskCheckDecision(
								Severity.INFO,
								CheckStatus.PASS,
								"Security config risk check passed",
								null
						)));
	}

	private String normalize(String targetText) {
		return targetText == null ? "" : targetText.toUpperCase(Locale.ROOT);
	}
}
