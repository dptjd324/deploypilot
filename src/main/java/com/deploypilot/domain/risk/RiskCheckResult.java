package com.deploypilot.domain.risk;

import com.deploypilot.domain.gate.CheckStatus;
import com.deploypilot.domain.gate.Severity;
import com.deploypilot.domain.release.Release;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "risk_check_result")
public class RiskCheckResult {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "release_id", nullable = false)
	private Release release;

	@Enumerated(EnumType.STRING)
	@Column(name = "risk_type", nullable = false, length = 50)
	private RiskType riskType;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private Severity severity;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 50)
	private CheckStatus status;

	@Column(nullable = false, length = 500)
	private String message;

	@Column(name = "detected_pattern", length = 200)
	private String detectedPattern;

	@Column(name = "target_text", columnDefinition = "TEXT")
	private String targetText;

	@Column(name = "checked_at", nullable = false)
	private LocalDateTime checkedAt;

	protected RiskCheckResult() {
	}

	private RiskCheckResult(
			Release release,
			RiskType riskType,
			Severity severity,
			CheckStatus status,
			String message,
			String detectedPattern,
			String targetText
	) {
		this.release = release;
		this.riskType = riskType;
		this.severity = severity;
		this.status = status;
		this.message = message;
		this.detectedPattern = detectedPattern;
		this.targetText = targetText;
	}

	public static RiskCheckResult create(
			Release release,
			RiskType riskType,
			Severity severity,
			CheckStatus status,
			String message,
			String detectedPattern,
			String targetText
	) {
		return new RiskCheckResult(release, riskType, severity, status, message, detectedPattern, targetText);
	}

	@PrePersist
	void prePersist() {
		this.checkedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public Release getRelease() {
		return release;
	}

	public RiskType getRiskType() {
		return riskType;
	}

	public Severity getSeverity() {
		return severity;
	}

	public CheckStatus getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public String getDetectedPattern() {
		return detectedPattern;
	}

	public String getTargetText() {
		return targetText;
	}

	public LocalDateTime getCheckedAt() {
		return checkedAt;
	}
}
