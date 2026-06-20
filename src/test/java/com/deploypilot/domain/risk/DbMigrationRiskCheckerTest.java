package com.deploypilot.domain.risk;

import static org.assertj.core.api.Assertions.assertThat;

import com.deploypilot.domain.gate.CheckStatus;
import com.deploypilot.domain.gate.Severity;
import org.junit.jupiter.api.Test;

class DbMigrationRiskCheckerTest {

	private final DbMigrationRiskChecker checker = new DbMigrationRiskChecker();

	@Test
	void checkReturnsBlockerForDestructiveMigration() {
		RiskCheckDecision decision = checker.check("ALTER TABLE orders DROP COLUMN legacy_code;");

		assertThat(decision.status()).isEqualTo(CheckStatus.FAIL);
		assertThat(decision.severity()).isEqualTo(Severity.BLOCKER);
		assertThat(decision.detectedPattern()).isEqualTo("DROP COLUMN");
	}

	@Test
	void checkReturnsWarnForMigrationThatNeedsReview() {
		RiskCheckDecision decision = checker.check("ALTER TABLE orders ADD COLUMN memo VARCHAR(255);");

		assertThat(decision.status()).isEqualTo(CheckStatus.WARN);
		assertThat(decision.severity()).isEqualTo(Severity.WARNING);
		assertThat(decision.detectedPattern()).isEqualTo("ALTER TABLE");
	}

	@Test
	void checkReturnsPassWhenNoRiskPatternExists() {
		RiskCheckDecision decision = checker.check("CREATE TABLE orders (id BIGINT PRIMARY KEY);");

		assertThat(decision.status()).isEqualTo(CheckStatus.PASS);
		assertThat(decision.severity()).isEqualTo(Severity.INFO);
		assertThat(decision.detectedPattern()).isNull();
	}
}
