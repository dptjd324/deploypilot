package com.deploypilot.domain.risk;

import static org.assertj.core.api.Assertions.assertThat;

import com.deploypilot.domain.gate.CheckStatus;
import com.deploypilot.domain.gate.Severity;
import org.junit.jupiter.api.Test;

class SecurityConfigRiskCheckerTest {

	private final SecurityConfigRiskChecker checker = new SecurityConfigRiskChecker();

	@Test
	void checkReturnsBlockerForPermitAllPattern() {
		RiskCheckDecision decision = checker.check("requestMatchers(\"/**\").permitAll()");

		assertThat(decision.status()).isEqualTo(CheckStatus.FAIL);
		assertThat(decision.severity()).isEqualTo(Severity.BLOCKER);
		assertThat(decision.detectedPattern()).isEqualTo("PERMITALL");
	}

	@Test
	void checkReturnsWarningForBasicAuthPattern() {
		RiskCheckDecision decision = checker.check("http.httpBasic()");

		assertThat(decision.status()).isEqualTo(CheckStatus.WARN);
		assertThat(decision.severity()).isEqualTo(Severity.WARNING);
		assertThat(decision.detectedPattern()).isEqualTo("BASIC");
	}

	@Test
	void checkReturnsPassWhenNoRiskPatternExists() {
		RiskCheckDecision decision = checker.check("authorizeHttpRequests(auth -> auth.anyRequest().authenticated())");

		assertThat(decision.status()).isEqualTo(CheckStatus.PASS);
		assertThat(decision.severity()).isEqualTo(Severity.INFO);
		assertThat(decision.detectedPattern()).isNull();
	}
}
